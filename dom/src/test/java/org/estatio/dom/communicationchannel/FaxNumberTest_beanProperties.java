package org.estatio.dom.communicationchannel;

import org.junit.Test;

import org.estatio.dom.AbstractBeanPropertiesTest;

public class FaxNumberTest_beanProperties extends AbstractBeanPropertiesTest {

	@Test
	public void test() {
	    newPojoTester()
	        .exercise(new FaxNumber());
	}

}
