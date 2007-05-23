/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2007 The OpenNMS Group, Inc. All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */
package org.opennms.netmgt.ticketd;

import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.TroubleTicketState;
import org.opennms.netmgt.ticketd.Ticket.State;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;

/**
 * OpenNMS Trouble Ticket API implementation.
 * 
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 *
 */
public class DefaultTicketerServiceLayer implements TicketerServiceLayer, InitializingBean {
	
	private AlarmDao m_alarmDao;
    private TicketerPlugin m_ticketerPlugin;

	/**
	 * Needs access to the AlarmDao.
	 * 
	 * @param alarmDao
	 */
	public void setAlarmDao(AlarmDao alarmDao) {
		m_alarmDao = alarmDao;
	}
    
    /**
     * Needs access to the TicketerPlugin API implementation for
     * communication with the HelpDesk.
     * 
     * @param ticketerPlugin
     */
    public void setTicketerPlugin(TicketerPlugin ticketerPlugin) {
        m_ticketerPlugin = ticketerPlugin;
    }
    
    public void afterPropertiesSet() throws Exception {
        Assert.state(m_alarmDao != null, "alarmDao property must be set");
        Assert.state(m_ticketerPlugin != null, "ticketPlugin property must be set");
    }

	public void cancelTicketForAlarm(int alarmId, String ticketId) {
		OnmsAlarm alarm = m_alarmDao.get(alarmId);
		if (alarm == null) {
			throw new ObjectRetrievalFailureException("Unable to locate Alarm with ID: "+alarmId, null);
		}

		setTicketState(ticketId, Ticket.State.CANCELLED);
        
        alarm.setTTicketState(TroubleTicketState.CANCELLED);
        m_alarmDao.saveOrUpdate(alarm);
        
	}

    private void setTicketState(String ticketId, State state) { 
        Ticket ticket = m_ticketerPlugin.get(ticketId);
        ticket.setState(state);
        m_ticketerPlugin.saveOrUpdate(ticket);
    }
    
    
	public void closeTicketForAlarm(int alarmId, String ticketId) {
		OnmsAlarm alarm = m_alarmDao.get(alarmId);
        
       setTicketState(ticketId, State.CLOSED);
        
		alarm.setTTicketState(TroubleTicketState.CLOSED);
		m_alarmDao.saveOrUpdate(alarm);
	}

	public void createTicketForAlarm(int alarmId) {
		OnmsAlarm alarm = m_alarmDao.get(alarmId);
        
        Ticket ticket = createTicketFromAlarm(alarm);
        
        m_ticketerPlugin.saveOrUpdate(ticket);

        alarm.setTTicketId(ticket.getId());
		alarm.setTTicketState(TroubleTicketState.OPEN);
		m_alarmDao.saveOrUpdate(alarm);
	}

    private Ticket createTicketFromAlarm(OnmsAlarm alarm) {
        Ticket ticket = new Ticket();
        ticket.setSummary(alarm.getLogMsg());
        ticket.setDetails(alarm.getDescription());
        ticket.setId(alarm.getTTicketId());
        return ticket;
    }

	public void updateTicketForAlarm(int alarmId, String ticketId) {
		OnmsAlarm alarm = m_alarmDao.get(alarmId);
        
		Ticket ticket = m_ticketerPlugin.get(ticketId);
        ticket.setState(State.OPEN);
        // TODO what do I do on an update?
        ticket.setDetails(alarm.getDescription());
        m_ticketerPlugin.saveOrUpdate(ticket);
        
		alarm.setTTicketState(TroubleTicketState.OPEN);
		m_alarmDao.saveOrUpdate(alarm);
	}
    
    // TODO what if the alarm doesn't exist?

}
