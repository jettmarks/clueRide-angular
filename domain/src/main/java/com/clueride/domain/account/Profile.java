/**
 *   Copyright 2015 Jett Marks
 * 
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
 *
 * Created Jul 28, 2015
 */
package com.clueride.domain.account;

/**
 * Ratings are based on a standard method of scoring various factors such as
 * hilliness, distance, facility type, and more subjective factors, but some
 * users may not care about hills and may avoid segregated trails; a Profile
 * allows adjusting the rating based on a User's preferences.
 *
 * This object should be DTO with some checks for consistency and validation;
 * computation of overall score would be outside of this class's responsibility.
 * 
 * @author jett
 */
public interface Profile {
    Double getHillFactor();

    Double getDistanceFactor();

    Double getFacilityFactor();
}
