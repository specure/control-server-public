package com.specure.core.enums;

import com.specure.common.enums.PackageType;
import com.specure.common.exception.PackageTypeException;
import com.specure.core.TestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
public class PackageTypeTest {

    @Test(expected = PackageTypeException.class)
    public void whenFromString_getWrongPackageType_ExpectExpectation() {
        PackageType.fromString("wrong");
    }

    @Test
    public void whenFromString_getProperPackageType_ExpectPackageType() {
        PackageType packageType = PackageType.fromString(TestConstants.DEFAULT_PACKAGE_TYPE_STRING);
        assertEquals(PackageType.MOBILE_PREPAID, packageType);
    }
}
