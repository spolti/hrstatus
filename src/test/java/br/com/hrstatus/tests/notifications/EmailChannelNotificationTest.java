/*
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.hrstatus.tests.notifications;

import br.com.hrstatus.utils.notification.ChannelSelector;
import br.com.hrstatus.utils.notification.DestinationSelector;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:spoltin@hrstatus.com.br">Filippe Spolti</a>
 */
public class EmailChannelNotificationTest {

    private final String MESSAGE = "This is a test message";
    private final String RECEIVER = "spolti@hrstatus.com.br";
    private final String SUBJECT = "this is a test subject";
    private final String JNDI = "java:jboss/mail/HrStatus";

    @Test
    public void testAllParameters() throws NoSuchFieldException, IllegalAccessException {

        ChannelSelector cns = new ChannelSelector(MESSAGE, RECEIVER, SUBJECT, JNDI);

        Field message = ChannelSelector.class.getDeclaredField("message");
        message.setAccessible(true);
        Assert.assertEquals(MESSAGE, (String) message.get(cns));

        Field receiver = ChannelSelector.class.getDeclaredField("receiver");
        receiver.setAccessible(true);
        Assert.assertEquals(RECEIVER, (String) receiver.get(cns));

        Field subject = ChannelSelector.class.getDeclaredField("subject");
        subject.setAccessible(true);
        Assert.assertEquals(SUBJECT, (String) subject.get(cns));

        Field jndi = ChannelSelector.class.getDeclaredField("jndi");
        jndi.setAccessible(true);
        Assert.assertEquals(JNDI, (String) jndi.get(cns));
    }

    @Test
    public void testDefaultParameter() throws IllegalAccessException, NoSuchFieldException {

        DestinationSelector ds = new DestinationSelector(MESSAGE, "", JNDI);

        Field message = DestinationSelector.class.getDeclaredField("message");
        message.setAccessible(true);
        Assert.assertEquals(MESSAGE, (String) message.get(ds));

        Field subject = DestinationSelector.class.getDeclaredField("subject");
        subject.setAccessible(true);
        Assert.assertEquals("HrStatus", (String) subject.get(ds));

        Field jndi = DestinationSelector.class.getDeclaredField("jndi");
        jndi.setAccessible(true);
        Assert.assertEquals(JNDI, (String) jndi.get(ds));
    }

    @Test
    public void testOverrideDefaultParameter() throws NoSuchFieldException, IllegalAccessException {

        DestinationSelector ds = new DestinationSelector(MESSAGE, SUBJECT, JNDI);

        Field message = DestinationSelector.class.getDeclaredField("message");
        message.setAccessible(true);
        Assert.assertEquals(MESSAGE, (String) message.get(ds));

        Field subject = DestinationSelector.class.getDeclaredField("subject");
        subject.setAccessible(true);
        Assert.assertEquals(SUBJECT, (String) subject.get(ds));

        Field jndi = DestinationSelector.class.getDeclaredField("jndi");
        jndi.setAccessible(true);
        Assert.assertEquals(JNDI, (String) jndi.get(ds));

    }
}