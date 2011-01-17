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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.testatoo.core.matcher.Matchers.containsExactlyText;
import static org.testatoo.core.matcher.Matchers.containsText;
import static org.testatoo.core.matcher.mock.MockFactory.format;
import static org.testatoo.core.matcher.mock.MockFactory.simpleTextFieldWithText;

public class ContainsTextTest {

    @Test
    public void test_containsText_matcher() {
        assertThat(simpleTextFieldWithText("MyText"), containsExactlyText("MyText"));
        assertThat(simpleTextFieldWithText("MyText"), containsText("Text"));

        try {
            assertThat(simpleTextFieldWithText("MyText"), containsText("NoData"));
            fail();
        } catch (AssertionError e) {
            assertThat(format(e.getMessage()), is("Expected: contains text:NoData got: <class org.testatoo.core.component.TextField with state : enabled:true, visible:true, value:MyText, label:label, maxLength:50>"));
        }
    }

}
