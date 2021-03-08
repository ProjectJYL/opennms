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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.opennms.web.enlinkd.BridgeLinkRemoteNode;

@XmlRootElement(name="bridgeLinkNode")
@JsonRootName("bridgeLinkNode")
public class BridgeLinkNodeDTO {
    @XmlElement(name="bridgeLocalPort")
    @JsonProperty("bridgeLocalPort")
    private String bridgeLocalPort;

    @XmlElement(name="bridgeLocalPortUrl")
    @JsonProperty("bridgeLocalPortUrl")
    private String bridgeLocalPortUrl;

    @XmlElement(name="BridgeLinkRemoteNodes")
    @JsonProperty("BridgeLinkRemoteNodes")
    private List<BridgeLinkRemoteNode> bridgeLinkRemoteNodes = new ArrayList<BridgeLinkRemoteNode>();

    @XmlElement(name="bridgeInfo")
    @JsonProperty("bridgeInfo")
    private String bridgeInfo;

    @XmlElement(name="bridgeLinkCreateTime")
    @JsonProperty("bridgeLinkCreateTime")
    private String bridgeLinkCreateTime;

    @XmlElement(name="bridgeLinkLastPollTime")
    @JsonProperty("bridgeLinkLastPollTime")
    private String bridgeLinkLastPollTime;

    public String getBridgeLocalPort() {
        return bridgeLocalPort;
    }

    public void setBridgeLocalPort(String bridgeLocalPort) {
        this.bridgeLocalPort = bridgeLocalPort;
    }

    public BridgeLinkNodeDTO withBridgeLocalPort(String bridgeLocalPort) {
        this.bridgeLocalPort = bridgeLocalPort;
        return this;
    }

    public String getBridgeLocalPortUrl() {
        return bridgeLocalPortUrl;
    }

    public void setBridgeLocalPortUrl(String bridgeLocalPortUrl) {
        this.bridgeLocalPortUrl = bridgeLocalPortUrl;
    }

    public BridgeLinkNodeDTO withBridgeLocalPortUrl(String bridgeLocalPortUrl) {
        this.bridgeLocalPortUrl = bridgeLocalPortUrl;
        return this;
    }

    public List<BridgeLinkRemoteNode> getBridgeLinkRemoteNodes() {
        return bridgeLinkRemoteNodes;
    }

    public void setBridgeLinkRemoteNodes(List<BridgeLinkRemoteNode> bridgeLinkRemoteNodes) {
        this.bridgeLinkRemoteNodes = bridgeLinkRemoteNodes;
    }

    public BridgeLinkNodeDTO withBridgeLinkRemoteNodes(List<BridgeLinkRemoteNode> bridgeLinkRemoteNodes) {
        this.bridgeLinkRemoteNodes = bridgeLinkRemoteNodes;
        return this;
    }

    public String getBridgeInfo() {
        return bridgeInfo;
    }

    public void setBridgeInfo(String bridgeInfo) {
        this.bridgeInfo = bridgeInfo;
    }

    public BridgeLinkNodeDTO withBridgeInfo(String bridgeInfo) {
        this.bridgeInfo = bridgeInfo;
        return this;
    }

    public String getBridgeLinkCreateTime() {
        return bridgeLinkCreateTime;
    }

    public void setBridgeLinkCreateTime(String bridgeLinkCreateTime) {
        this.bridgeLinkCreateTime = bridgeLinkCreateTime;
    }

    public BridgeLinkNodeDTO withBridgeLinkCreateTime(String bridgeLinkCreateTime) {
        this.bridgeLinkCreateTime = bridgeLinkCreateTime;
        return this;
    }

    public String getBridgeLinkLastPollTime() {
        return bridgeLinkLastPollTime;
    }

    public void setBridgeLinkLastPollTime(String bridgeLinkLastPollTime) {
        this.bridgeLinkLastPollTime = bridgeLinkLastPollTime;
    }

    public BridgeLinkNodeDTO withBridgeLinkLastPollTime(String bridgeLinkLastPollTime) {
        this.bridgeLinkLastPollTime = bridgeLinkLastPollTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BridgeLinkNodeDTO that = (BridgeLinkNodeDTO) o;
        return Objects.equals(bridgeLocalPort, that.bridgeLocalPort) && Objects.equals(bridgeLocalPortUrl, that.bridgeLocalPortUrl) && Objects.equals(bridgeLinkRemoteNodes, that.bridgeLinkRemoteNodes) && Objects.equals(bridgeInfo, that.bridgeInfo) && Objects.equals(bridgeLinkCreateTime, that.bridgeLinkCreateTime) && Objects.equals(bridgeLinkLastPollTime, that.bridgeLinkLastPollTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bridgeLocalPort, bridgeLocalPortUrl, bridgeLinkRemoteNodes, bridgeInfo, bridgeLinkCreateTime, bridgeLinkLastPollTime);
    }
}
