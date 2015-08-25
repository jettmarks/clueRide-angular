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
 * Created Aug 21, 2015
 */
package com.clueride.domain.dev;

/**
 * Summary of a Node's relationship with a particular network.
 *
 * If a Node's state is one of the following, the corresponding geometry will be
 * useful.
 * <UL>
 * <LI>ON_SEGMENT - The Segment to be split by the Node.
 * <LI>ON_SINGLE_TRACK - The sole Track which connects the Node to the Network.
 * <LI>ON_MULTI_TRACK - A List<Track> which connect the Node to the Network.
 * </UL>
 * 
 * @author jett
 */
public enum NodeNetworkState {
	UNDEFINED, ON_NETWORK, ON_SEGMENT, ON_SINGLE_TRACK, ON_MULTI_TRACK, OFF_NETWORK
}
