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

package com.exactprosystems.clearth.connectivity.connections2.settings;

import com.exactprosystems.clearth.connectivity.connections.ClearThConnectionSettings;

@ConnectionSettings(columns = {"host", "login", "mode"},
		order = {"login", "password"})
public class CorrectSettings extends ClearThConnectionSettings<CorrectSettings>
{
	//Fields cover various data types and field properties, i.e. custom name, inputType, etc.
	
	@ConnectionSetting
	private String host;
	
	@ConnectionSetting
	private int port;
	
	@ConnectionSetting(name = "Username")
	private String login;
	
	@ConnectionSetting(inputType = InputType.PASSWORD)
	private String password;
	
	private String dataCharset;
	
	@ConnectionSetting(name = "Connection mode")
	private ConnectionMode mode;
	
	@ConnectionSetting
	private long timeout;
	
	@ConnectionSetting
	private boolean autoReconnect;
	
	@Override
	public CorrectSettings copy()
	{
		CorrectSettings result = new CorrectSettings();
		result.host = host;
		result.port = port;
		result.login = login;
		result.password = password;
		result.dataCharset = dataCharset;
		result.mode = mode;
		result.timeout = timeout;
		result.autoReconnect = autoReconnect;
		return result;
	}
	
	
	public String getHost()
	{
		return host;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}
	
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	
	public String getLogin()
	{
		return login;
	}
	
	public void setLogin(String login)
	{
		this.login = login;
	}
	
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	
	public String getDataCharset()
	{
		return dataCharset;
	}
	
	public void setDataCharset(String dataCharset)
	{
		this.dataCharset = dataCharset;
	}
	
	
	public ConnectionMode getMode()
	{
		return mode;
	}
	
	public void setMode(ConnectionMode mode)
	{
		this.mode = mode;
	}
	
	
	public long getTimeout()
	{
		return timeout;
	}
	
	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
	
	
	public boolean isAutoReconnect()
	{
		return autoReconnect;
	}
	
	public void setAutoReconnect(boolean autoReconnect)
	{
		this.autoReconnect = autoReconnect;
	}
}