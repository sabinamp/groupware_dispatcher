# Groupware Dispatcher Repository

JavaFX Application used by the dispatcher for order management, delivery management and tracking.
- using HiveMQ MQTT Client library the Async API Builder method: buildAsync()

HiveMQ MQTT Client is an MQTT 5.0 and MQTT 3.1.1 compatible and feature-rich Java client library.
HiveMQ fully supports the following MQTT versions: MQTT 3.1, MQTT 3.1.1, MQTT 5.0. This project uses MQTT 3.1.1

The Groupware Dispatcher Java Client connects to the broker and publishes requests for data related to orders and couriers.
It checks every incoming message and updates the user interface accordingly.

# Message Broker
* Download the latest HiveMQ Community Edition archive from https://www.hivemq.com/developers/community/
* Unpack
* Run start HiveMQ (run.bat on a Windows computer within the folder C:\hivemqbroker\hivemq-community-edition\build\zip\hivemq-ce-2020.3-SNAPSHOT\bin)
* HiveMQ User Guide https://www.hivemq.com/docs/hivemq/4.3/user-guide/introduction.html

# Groupware Store Repository -  Backend - Java Client
The backend Java service connects to the broker, subscribes and listens to 'orders/all_info/get/#' and 'couriers/info/get/#' . It publishes the response to the topic
'orders/all_info/get/+/response' and respectively 'couriers/info/get/+/response'.

# Further HiveMQ Documentation
HiveMQ Documentation / User Guide https://www.hivemq.com/docs/hivemq/4.3/user-guide/introduction.html
HiveMQ Community Edition Github https://github.com/hivemq/hivemq-community-edition 
HiveMQ MQTT Java Client https://hivemq.github.io/hivemq-mqtt-client/