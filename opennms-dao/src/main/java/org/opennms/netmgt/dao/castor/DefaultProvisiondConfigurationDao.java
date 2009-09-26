/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2009 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 * 
 * Created: September 11, 2009
 *
 * Copyright (C) 2009 The OpenNMS Group, Inc.  All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */
package org.opennms.netmgt.dao.castor;

import java.util.List;

import org.opennms.netmgt.config.provisiond.ProvisiondConfiguration;
import org.opennms.netmgt.config.provisiond.RequisitionDef;
import org.opennms.netmgt.dao.ProvisiondConfigurationDao;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * Default implementation of <code>AckdConfiguration</code> containing utility methods for manipulating
 * the <code>Ackd</code> and <code>AckdReader</code>s.
 * 
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 */
public class DefaultProvisiondConfigurationDao extends AbstractCastorConfigDao<ProvisiondConfiguration, ProvisiondConfiguration> implements ProvisiondConfigurationDao {

    public DefaultProvisiondConfigurationDao() {
        super(ProvisiondConfiguration.class, "Provisiond Configuration");
    }
    
    public ProvisiondConfiguration getConfig() {
        return getContainer().getObject();
    }

    @Override
    public ProvisiondConfiguration translateConfig(ProvisiondConfiguration castorConfig) {
        return castorConfig;
    }

    /**
     * The exception boils up from the container class  The container class should
     * indicate this.
     * 
     */
    public void reloadConfiguration() throws DataAccessResourceFailureException {
        getContainer().reload();
    }

    public RequisitionDef getDef(String defName) {
        for (RequisitionDef def : getDefs()) {
            if (def.getImportName().equals(defName)) {
                return def;
            }
        }
        return null;
    }

    public List<RequisitionDef> getDefs() {
        return getConfig().getRequisitionDefCollection();
    }

    public String getForeignSourceDir() {
        return getConfig().getForeignSourceDir();
    }

    public String getRequisitionDir() {
        return getConfig().getRequistionDir();
    }

    public Integer getImportThreads() {
        return Integer.valueOf((int) getConfig().getImportThreads());
    }

    public Integer getScanThreads() {
        return Integer.valueOf((int)getConfig().getScanThreads());
    }

    public Integer getRescanThreads() {
        return Integer.valueOf((int)getConfig().getRescanThreads());
    }

    public Integer getWriteThreads() {
        return Integer.valueOf((int)getConfig().getWriteThreads());
    }
    
}
