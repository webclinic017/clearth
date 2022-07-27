/******************************************************************************
 * Copyright 2009-2022 Exactpro Systems Limited
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

package com.exactprosystems.clearth.connectivity.listeners;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.exactprosystems.clearth.connectivity.iface.ClearThMessage;
import com.exactprosystems.clearth.connectivity.iface.ICodec;
import com.exactprosystems.clearth.messages.CollectorMessageSource;
import com.exactprosystems.clearth.utils.SettingsException;
import com.exactprosystems.clearth.utils.SimpleKeyValueCodec;

import static com.exactprosystems.clearth.connectivity.iface.ClearThMessage.MSGTYPE;
import static com.exactprosystems.clearth.connectivity.listeners.ClearThMessageCollector.DEFAULT_MESSAGE_END_INDICATOR;

public class ClearThCollectorSourceTest {
	private static final Path COLLECTOR_SOURCE_TEST_OUTPUT_DIR = Paths.get("src/test/resources/CollectorMessageSourceTest");

	private static final String MESSAGES_FILE = "messages.txt";
	private static final String MESSAGES_DELIMITER = "\n\n\n";
	
	private ICodec codec;

	@BeforeClass
	public void init()
	{
		codec = new SimpleKeyValueCodec();
	}

	@DataProvider(name = "two-stage-collectors-and-messages")
	public Object[][] createListenersAndMessagesData()
			throws IOException, SettingsException
	{
		String[] messages = getMessagesFromFile(MESSAGES_FILE);

		ClearThMessageCollector defaultListener = createListener("Default", codec, new HashMap<>());
		CollectorMessageSource defaultCollector = createSource(defaultListener, -1, true);
		CollectorMessageSource reversedCollector = createSource(defaultListener, -1, false);

		//constructor with time arguments requiers skipped messages to already exist to function correctly
		ClearThMessageCollector defaultTimedListener = createListener("Default2", codec, new HashMap<>());
		putMessages(defaultTimedListener, messages, 0, 2);
		CollectorMessageSource defaultTimedCollector = createSource(defaultTimedListener, 0, true);
		
		return new Object[][]
			{
				{defaultCollector, defaultListener, messages, Arrays.asList("AAA", "BBB", "CCC", "DDD"), Arrays.asList("EEE", "FFF", "GGG"), 0, 4},
				{reversedCollector, defaultListener, messages, Arrays.asList("DDD", "CCC", "BBB", "AAA"), Arrays.asList("GGG", "FFF", "EEE"), 0, 4},
				{defaultTimedCollector, defaultTimedListener, messages, Arrays.asList("BBB", "CCC", "DDD"), Arrays.asList("EEE", "FFF", "GGG"), 2, 4}
			};
	}

	@Test(dataProvider = "two-stage-collectors-and-messages")
	public void checkCollectorReadingOrder(CollectorMessageSource source, ClearThMessageCollector listener, 
			String[] messages, List<String> expectedFirst, List<String> expectedSecond, int firstBegin, int firstEnd) 
			throws IOException
	{
		putMessages(listener, messages, firstBegin, firstEnd);
		
		List<String> actual = getMessageTypes(source);
		
		Assert.assertEquals(actual, expectedFirst);

		putMessages(listener, messages, firstEnd, messages.length);
		actual = getMessageTypes(source);
		
		Assert.assertEquals(actual, expectedSecond);
	}

	private CollectorMessageSource createSource(ClearThMessageCollector listener, long afterTime, boolean directOrder) 
			throws SettingsException
	{
		if (afterTime < 0) {
			return new CollectorMessageSource(listener, directOrder);
		}
		return new CollectorMessageSource(listener, afterTime, directOrder);
	}

	private ClearThMessageCollector createListener(String name, ICodec codec, Map<String, String> settings)
			throws SettingsException
	{
		return new ClearThMessageCollector(name, "con", codec, settings, DEFAULT_MESSAGE_END_INDICATOR);
	}
	
	private String[] getMessagesFromFile(String fileName) throws IOException
	{
		File file = COLLECTOR_SOURCE_TEST_OUTPUT_DIR.resolve(fileName).toFile();
		return FileUtils.readFileToString(file, Charset.defaultCharset()).split(MESSAGES_DELIMITER);
	}
	
	private void putMessages(ClearThMessageCollector listener, String[] messages, int first, int last) {
		for (int i = first; i < last; ++i) {
			listener.onMessageReceived(messages[i], i);
		}
	}

	private List<String> getMessageTypes(CollectorMessageSource source) throws IOException {
		ClearThMessage<?> cur = null;
		List<String> res = new ArrayList<>();
		while ((cur = source.nextMessage()) != null) {
			res.add(cur.getField(MSGTYPE));
			source.removeMessage();
		}
		return res;
	}
}