/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.starburstdata.udfs.encrypt;

import io.trino.operator.scalar.AbstractTestFunctions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.trino.spi.type.VarcharType.VARCHAR;

public class TestEncrypt
        extends AbstractTestFunctions
{
    @BeforeClass
    public void setUp()
            throws Exception
    {
        registerScalar(EncryptDecrypt.class);
    }

    @Test
    public void testEncrypt()
    {
        assertFunction("encrypt('myvalue','mypassword')", VARCHAR, "/stEnUn+cUs=");
        assertFunction("decrypt('/stEnUn+cUs=','mypassword')", VARCHAR, "myvalue");
    }
}
