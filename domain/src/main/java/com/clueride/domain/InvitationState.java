package com.clueride.domain;/*
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
 * Created by jett on 5/29/16.
 */

/**
 * State of an Invitation:
 * <ul>
 *     <li>INITIAL: After creation and through the sending of the invite</li>
 *     <li>ACTIVE: Accepted and session established</li>
 *     <li>DECLINED: Invitation declined; may be reversed by guest</li>
 *     <li>EXPIRED: Outing has completed; no longer valid.</li>
 * </ul>
 */
public enum InvitationState {
    INITIAL,
    ACTIVE,
    DECLINED,
    EXPIRED
}
