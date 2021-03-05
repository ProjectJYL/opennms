/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2021 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2021 The OpenNMS Group, Inc.
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

package org.opennms.web.rest.v2.models;

import java.util.Objects;

public class CdpLinkNodeDTO {

    private String  cdpLocalPort;
    private String  cdpLocalPortUrl;

    private String cdpCacheDevice;
    private String cdpCacheDeviceUrl;

    private String cdpCacheDevicePort;
    private String cdpCacheDevicePortUrl;

    private String cdpCachePlatform;

    private String cdpCreateTime;
    private String cdpLastPollTime;

    public String getCdpLocalPort() {
        return cdpLocalPort;
    }

    public void setCdpLocalPort(String cdpLocalPort) {
        this.cdpLocalPort = cdpLocalPort;
    }

    public CdpLinkNodeDTO withCdpLocalPort(String cdpLocalPort) {
        this.cdpLocalPort = cdpLocalPort;
        return this;
    }

    public String getCdpLocalPortUrl() {
        return cdpLocalPortUrl;
    }

    public void setCdpLocalPortUrl(String cdpLocalPortUrl) {
        this.cdpLocalPortUrl = cdpLocalPortUrl;
    }

    public CdpLinkNodeDTO withCdpLocalPortUrl(String cdpLocalPortUrl) {
        this.cdpLocalPortUrl = cdpLocalPortUrl;
        return this;
    }

    public String getCdpCacheDevice() {
        return cdpCacheDevice;
    }

    public void setCdpCacheDevice(String cdpCacheDevice) {
        this.cdpCacheDevice = cdpCacheDevice;
    }

    public CdpLinkNodeDTO withCdpCacheDevice(String cdpCacheDevice) {
        this.cdpCacheDevice = cdpCacheDevice;
        return this;
    }

    public String getCdpCacheDeviceUrl() {
        return cdpCacheDeviceUrl;
    }

    public void setCdpCacheDeviceUrl(String cdpCacheDeviceUrl) {
        this.cdpCacheDeviceUrl = cdpCacheDeviceUrl;
    }

    public CdpLinkNodeDTO withCdpCacheDeviceUrl(String cdpCacheDeviceUrl) {
        this.cdpCacheDeviceUrl = cdpCacheDeviceUrl;
        return this;
    }

    public String getCdpCacheDevicePort() {
        return cdpCacheDevicePort;
    }

    public void setCdpCacheDevicePort(String cdpCacheDevicePort) {
        this.cdpCacheDevicePort = cdpCacheDevicePort;
    }

    public CdpLinkNodeDTO withCdpCacheDevicePort(String cdpCacheDevicePort) {
        this.cdpCacheDevicePort = cdpCacheDevicePort;
        return this;
    }

    public String getCdpCacheDevicePortUrl() {
        return cdpCacheDevicePortUrl;
    }

    public void setCdpCacheDevicePortUrl(String cdpCacheDevicePortUrl) {
        this.cdpCacheDevicePortUrl = cdpCacheDevicePortUrl;
    }

    public CdpLinkNodeDTO withCdpCacheDevicePortUrl(String cdpCacheDevicePortUrl) {
        this.cdpCacheDevicePortUrl = cdpCacheDevicePortUrl;
        return this;
    }

    public String getCdpCachePlatform() {
        return cdpCachePlatform;
    }

    public void setCdpCachePlatform(String cdpCachePlatform) {
        this.cdpCachePlatform = cdpCachePlatform;
    }

    public CdpLinkNodeDTO withCdpCachePlatform(String cdpCachePlatform) {
        this.cdpCachePlatform = cdpCachePlatform;
        return this;
    }

    public String getCdpCreateTime() {
        return cdpCreateTime;
    }

    public void setCdpCreateTime(String cdpCreateTime) {
        this.cdpCreateTime = cdpCreateTime;
    }

    public CdpLinkNodeDTO withCdpCreateTime(String cdpCreateTime) {
        this.cdpCreateTime = cdpCreateTime;
        return this;
    }

    public String getCdpLastPollTime() {
        return cdpLastPollTime;
    }

    public void setCdpLastPollTime(String cdpLastPollTime) {
        this.cdpLastPollTime = cdpLastPollTime;
    }

    public CdpLinkNodeDTO withCdpLastPollTime(String cdpLastPollTime) {
        this.cdpLastPollTime = cdpLastPollTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CdpLinkNodeDTO that = (CdpLinkNodeDTO) o;
        return Objects.equals(cdpLocalPort, that.cdpLocalPort) && Objects.equals(cdpLocalPortUrl, that.cdpLocalPortUrl) && Objects.equals(cdpCacheDevice, that.cdpCacheDevice) && Objects.equals(cdpCacheDeviceUrl, that.cdpCacheDeviceUrl) && Objects.equals(cdpCacheDevicePort, that.cdpCacheDevicePort) && Objects.equals(cdpCacheDevicePortUrl, that.cdpCacheDevicePortUrl) && Objects.equals(cdpCachePlatform, that.cdpCachePlatform) && Objects.equals(cdpCreateTime, that.cdpCreateTime) && Objects.equals(cdpLastPollTime, that.cdpLastPollTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cdpLocalPort, cdpLocalPortUrl, cdpCacheDevice, cdpCacheDeviceUrl, cdpCacheDevicePort, cdpCacheDevicePortUrl, cdpCachePlatform, cdpCreateTime, cdpLastPollTime);
    }
}
