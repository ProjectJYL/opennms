/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2016 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2016 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.features.geolocation.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opennms.core.criteria.Criteria;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.features.geocoder.GeocoderConfigurationException;
import org.opennms.features.geocoder.GeocoderResult;
import org.opennms.features.geocoder.GeocoderService;
import org.opennms.features.geocoder.GeocoderServiceManager;
import org.opennms.features.geolocation.api.Coordinates;
import org.opennms.features.geolocation.api.GeolocationResolver;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class DefaultGeolocationResolver implements GeolocationResolver {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultGeolocationResolver.class);

    private final NodeDao nodeDao;
    private final GeocoderServiceManager geocoderServiceManager;

    public DefaultGeolocationResolver(GeocoderServiceManager geocoderServiceManager, NodeDao nodeDao) {
        this.geocoderServiceManager = Objects.requireNonNull(geocoderServiceManager);
        this.nodeDao = Objects.requireNonNull(nodeDao);
    }

    @Override
    public Map<Integer, Coordinates> resolve(Collection<Integer> nodeIds) {
        if (nodeIds == null || nodeIds.isEmpty()) {
            return new HashMap<>(); // nothing to do
        }

        // Lookup all nodes and gather the address string
        final Criteria criteria = new CriteriaBuilder(OnmsNode.class).in("id", nodeIds).toCriteria();
        final Map<Integer, String> nodeIdAddressMap = nodeDao.findMatching(criteria).stream()
                .filter(n -> n.getAsset("latitude") == null && n.getAsset("longitude") == null)
                .filter(n -> !Strings.isNullOrEmpty(n.getGeoLocationAsAddressString()))
                .collect(Collectors.toMap(OnmsNode::getId, n -> n.getGeoLocationAsAddressString()));
        return resolve(nodeIdAddressMap);
    }

    @Override
    public Map<Integer, Coordinates> resolve(Map<Integer, String> nodeIdAddressMap) {
        if (nodeIdAddressMap == null || nodeIdAddressMap.isEmpty()) {
            return new HashMap<>(); // nothing to do
        }

        // 1st filter out invalid values
        nodeIdAddressMap = nodeIdAddressMap.entrySet().stream()
                .filter(e -> !Strings.isNullOrEmpty(e.getValue()) && e.getKey() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 2nd Resolve longitude/latitude coordinates from an address string
        final Map<Integer, Coordinates> resultMap = new HashMap<>();
        nodeIdAddressMap.entrySet().stream()
                .forEach(entry -> {
                    final String addressString = entry.getValue();
                    final Coordinates coordinates = resolve(addressString);
                    if (coordinates != null) {
                        resultMap.put(entry.getKey(), coordinates);
                    }
                });
        return resultMap;
    }

    @Override
    public Coordinates resolve(String addressString) {
        final GeocoderService activeGeocoder = geocoderServiceManager.getActiveGeocoderService();
        if (activeGeocoder == null) {
            LOG.warn("Error resolving address '{}': No active Geocoder", addressString);
            return null;
        }
        try {
            final GeocoderResult result = activeGeocoder.resolveAddress(addressString);
            if (result.hasError()) {
                LOG.error("Error resolving address '{}': {}", addressString, result.getThrowable().getMessage(), result.getThrowable());
                return null;
            }
            if (result.isEmpty()) {
                LOG.warn("Error resolving address '{}': Response was empty", addressString);
                return null;
            }
            org.opennms.features.geocoder.Coordinates coordinates = result.getCoordinates();
            LOG.debug("Successfully resolved address '{}': Active Geocoder with id '{}' resolved to long/lat: {}/{}", addressString, activeGeocoder.getId(), coordinates.getLongitude(), coordinates.getLatitude());
            return new Coordinates(coordinates.getLongitude(), coordinates.getLatitude());
        } catch (GeocoderConfigurationException ex) {
            LOG.warn("Error resolving address '{}': Active Geocoder with id '{}' is not configured properly", addressString, activeGeocoder.getId(), ex);
            return null;
        } catch (Exception ex) {
            LOG.warn("Error resolving address '{}': An unexpected exception occurred", addressString, ex);
            return null;
        }
    }
}
