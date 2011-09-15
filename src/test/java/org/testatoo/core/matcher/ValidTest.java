/**
 * Copyright (C) 2008 Ovea <dev@testatoo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testatoo.core.matcher;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.testatoo.core.matcher.Matchers.valid;
import static org.testatoo.core.matcher.mock.MockFactory.*;

/**
 * @author David Avenante
 */
public class ValidTest {

    @Test
    public void test_validity_matcher() {
        assertThat(validComponent(), is(valid()));
        assertThat(invalidComponent(), is(not(valid())));

        try {
            assertThat(invalidComponent(), is(valid()));
            fail();
        } catch (AssertionError e) {
            assertThat(format(e.getMessage()), is("Expected: is valid:true but: was <valid:false>"));
        }
    }

}