/*
 * Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.stdlib.io.nativeimpl;

import org.ballerinalang.jvm.JSONParser;
import org.ballerinalang.jvm.XMLFactory;
import org.ballerinalang.jvm.util.exceptions.BallerinaException;
import org.ballerinalang.jvm.values.MapValue;
import org.ballerinalang.jvm.values.MapValueImpl;
import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinalang.jvm.values.XMLValue;
import org.ballerinalang.jvm.values.api.BMap;
import org.ballerinalang.jvm.values.api.BObject;
import org.ballerinalang.jvm.values.api.BString;
import org.ballerinalang.jvm.values.utils.StringUtils;
import org.ballerinalang.stdlib.io.channels.base.Channel;
import org.ballerinalang.stdlib.io.channels.base.CharacterChannel;
import org.ballerinalang.stdlib.io.readers.CharacterChannelReader;
import org.ballerinalang.stdlib.io.utils.BallerinaIOException;
import org.ballerinalang.stdlib.io.utils.IOConstants;
import org.ballerinalang.stdlib.io.utils.IOUtils;
import org.ballerinalang.stdlib.io.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.ballerinalang.stdlib.io.utils.IOConstants.CHARACTER_CHANNEL_NAME;

/**
 * This class hold Java inter-ops bridging functions for io# *CharacterChannels.
 *
 * @since 1.1.0
 */
public class CharacterChannelUtils {

    private static final Logger log = LoggerFactory.getLogger(CharacterChannelUtils.class);

    private CharacterChannelUtils() {
    }

    public static void initCharacterChannel(ObjectValue characterChannel, ObjectValue byteChannelInfo,
                                            BString encoding) {
        try {
            Channel byteChannel = (Channel) byteChannelInfo.getNativeData(IOConstants.BYTE_CHANNEL_NAME);
            CharacterChannel bCharacterChannel = new CharacterChannel(byteChannel, encoding.getValue());
            characterChannel.addNativeData(CHARACTER_CHANNEL_NAME, bCharacterChannel);
        } catch (Exception e) {
            String message = "error occurred while converting byte channel to character channel: " + e.getMessage();
            log.error(message, e);
            throw IOUtils.createError(message);
        }
    }

    public static Object read(ObjectValue channel, long numberOfCharacters) {
        CharacterChannel characterChannel = (CharacterChannel) channel.getNativeData(CHARACTER_CHANNEL_NAME);
        if (characterChannel.hasReachedEnd()) {
            return IOUtils.createEoFError();
        } else {
            try {
                return org.ballerinalang.jvm.StringUtils.fromString(characterChannel.read((int) numberOfCharacters));
            } catch (BallerinaIOException e) {
                log.error("error occurred while reading characters.", e);
                return IOUtils.createError(e);
            }
        }
    }

    public static Object readJson(ObjectValue channel) {
        CharacterChannel charChannel = (CharacterChannel) channel.getNativeData(CHARACTER_CHANNEL_NAME);
        CharacterChannelReader reader = new CharacterChannelReader(charChannel);
        try {
            Object returnValue = JSONParser.parse(reader);
            if (returnValue instanceof String) {

                return org.ballerinalang.jvm.StringUtils.fromString((String) returnValue);
            }
            return returnValue;
        } catch (BallerinaException e) {
            log.error("unable to read json from character channel", e);
            return IOUtils.createError(e);
        }
    }

    public static Object readXml(ObjectValue channel) {
        CharacterChannel charChannel = (CharacterChannel) channel.getNativeData(CHARACTER_CHANNEL_NAME);
        CharacterChannelReader reader = new CharacterChannelReader(charChannel);
        try {
            return XMLFactory.parse(reader);
        } catch (BallerinaException e) {
            return IOUtils.createError(e);
        }
    }

    public static Object readProperty(ObjectValue channel, BString key) {
        CharacterChannel charChannel = (CharacterChannel) channel.getNativeData(CHARACTER_CHANNEL_NAME);
        CharacterChannelReader reader = new CharacterChannelReader(charChannel);
        return PropertyUtils.readProperty(reader, key);
    }

    public static Object readYaml(ObjectValue channel) {
        CharacterChannel charChannel = (CharacterChannel) channel.getNativeData(CHARACTER_CHANNEL_NAME);
        CharacterChannelReader reader = new CharacterChannelReader(charChannel);
        Yaml yaml = new Yaml();
        Object loadedYaml = yaml.load(reader);
        Map<String, Object> yamlMap = (Map<String, Object>) loadedYaml;
        Iterator<Map.Entry<String, Object>> itr = yamlMap.entrySet().iterator();
        MapValue<BString, Object> bMap = new MapValueImpl<>();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = itr.next();
            bMap.put(org.ballerinalang.jvm.StringUtils.fromString(entry.getKey()),
                    entry.getValue().toString()
            );
        }
        return bMap;
    }

    public static Object close(ObjectValue channel) {
        CharacterChannel charChannel = (CharacterChannel) channel.getNativeData(CHARACTER_CHANNEL_NAME);
        try {
            charChannel.close();
        } catch (ClosedChannelException e) {
            return IOUtils.createError("channel already closed.");
        } catch (IOException e) {
            return IOUtils.createError(e);
        }
        return null;
    }

    public static Object write(ObjectValue channel, BString content, long startOffset) {
        CharacterChannel characterChannel = (CharacterChannel) channel.getNativeData(CHARACTER_CHANNEL_NAME);
        try {
            return characterChannel.write(content.getValue(), (int) startOffset);
        } catch (IOException e) {
            return IOUtils.createError(e);
        }
    }

    public static Object writeJson(ObjectValue characterChannelObj, Object content) {
        try {
            CharacterChannel characterChannel = (CharacterChannel) characterChannelObj
                    .getNativeData(CHARACTER_CHANNEL_NAME);
            IOUtils.writeFull(characterChannel, StringUtils.getJsonString(content));
        } catch (BallerinaIOException e) {
            return IOUtils.createError(e);
        }
        return null;
    }

    public static Object writeXml(ObjectValue characterChannelObj, XMLValue content) {
        try {
            CharacterChannel characterChannel = (CharacterChannel) characterChannelObj
                    .getNativeData(CHARACTER_CHANNEL_NAME);
            IOUtils.writeFull(characterChannel, content.toString());
        } catch (BallerinaIOException e) {
            return IOUtils.createError(e);
        }
        return null;
    }

    public static Object storeProperties(ObjectValue characterChannelObj, BMap<BString, BString> properties) {
        try {
            CharacterChannel characterChannel = (CharacterChannel) characterChannelObj
                    .getNativeData(CHARACTER_CHANNEL_NAME);
            String content = PropertyUtils.getWritablePropertyContent(properties);
            IOUtils.writeFull(characterChannel, content);
        } catch (BallerinaIOException e) {
            return IOUtils.createError(e);
        }
        return null;
    }

    public static Object writeYaml(ObjectValue characterChannelObj, BMap<BString, BObject> yamlData) {
        try {
            CharacterChannel characterChannel = (CharacterChannel) characterChannelObj
                    .getNativeData(CHARACTER_CHANNEL_NAME);
            Iterator<Map.Entry<BString, BObject>> itr = yamlData.entrySet().iterator();
            Map<String, Object> jMap = new HashMap<>();
            while (itr.hasNext()) {
                Map.Entry<BString, BObject> entry = itr.next();
                jMap.put(entry.getKey().getValue(), entry.getValue().getNativeData());
            }

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            String output = yaml.dump(jMap);
            IOUtils.writeFull(characterChannel, output);
        } catch (BallerinaIOException e) {
            return IOUtils.createError(e);
        }
        return null;
    }
}
