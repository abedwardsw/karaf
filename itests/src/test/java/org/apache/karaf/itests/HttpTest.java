/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.itests;

import static org.junit.Assert.assertTrue;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Constants;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class HttpTest extends KarafTestSupport {

    @Before
    public void installHttpFeature() throws Exception {
    	installAndAssertFeature("webconsole");
    }

    @Test
    public void list() throws Exception {
        waitForService("(objectClass=javax.servlet.ServletContext)", 5000);
        assertContains("/system/console", executeCommand("http:list"));
    }

    @Test
    public void listViaMBean() throws Exception {
        JMXConnector connector = null;
        try {
            connector = this.getJMXConnector();
            MBeanServerConnection connection = connector.getMBeanServerConnection();
            ObjectName name = new ObjectName("org.apache.karaf:type=http,name=root");
            TabularData servlets = (TabularData) connection.getAttribute(name, "Servlets");
            assertTrue(servlets.size() > 0);
        } finally {
            close(connector);
        }
    }

}
