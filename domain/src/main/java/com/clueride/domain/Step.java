/*
 * Copyright 2016 Jett Marks
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
 *
 * Created by jett on 1/15/16.
 */
package com.clueride.domain;

import com.clueride.domain.user.Path;

/**
 * "Marker" interface for Locations and Paths which helps keep order.
 *
 * The next Step at a Location will be a {@link Path} (or choice of Paths).  The next Step while
 * on a Path will be arrival at the next Location.
 *
 * This allows an ordered list of Steps without necessarily worrying about whether the next
 * one comes from the Location List or the Path List, or whatever else we might want to
 * stick in a {@link Course}.
 */
public interface Step {

}
