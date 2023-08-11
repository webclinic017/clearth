/******************************************************************************
 * Copyright 2009-2023 Exactpro Systems Limited
 * https://www.exactpro.com
 * Build Software to Test Software
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
 ******************************************************************************/

package com.exactprosystems.clearth.data.th2.events;

import java.time.Instant;

import com.exactpro.th2.common.grpc.EventID;

public class Th2EventMetadata
{
	private final EventID id;
	private final Instant startTimestamp,
			endTimestamp;
	
	public Th2EventMetadata(EventID id, Instant startTimestamp, Instant endTimestamp)
	{
		this.id = id;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}
	
	public EventID getId()
	{
		return id;
	}
	
	public Instant getStartTimestamp()
	{
		return startTimestamp;
	}
	
	public Instant getEndTimestamp()
	{
		return endTimestamp;
	}
}
