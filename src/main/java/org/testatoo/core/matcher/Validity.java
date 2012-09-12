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

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.testatoo.core.component.Component;
import org.testatoo.core.nature.ValiditySupport;

/**
 * This class is a matcher (written based on hamcrest possibilites) to test the validity of a graphic object.
 *
 * @author dev@testatoo.org
 */
public class Validity extends TypeSafeMatcher<ValiditySupport> {

    /**
     * Uses the matcher Validity to compare evaluate the validity of o component,
     * using the syntax "valid()"
     *
     * @param validitySupport the graphic object that supports validity (validation)
     * @return True if is valid
     */
    @Override
    public boolean matchesSafely(ValiditySupport validitySupport) {
        return validitySupport.isValid();
    }

    /**
     * To append the description of the validity to the given description
     *
     * @param description the description may be part of a a description of a larger object
     */
    public void describeTo(Description description) {
        description.appendText("valid:true");
    }

    /**
     * Using the syntax "valid()", the test creates a Validity matcher
     *
     * @return a new Validity matcher
     */
    public static Validity valid() {
        return new Validity();
    }


}
