This is a Java Swing app that displays the temperature from zigbee and 433 MHz sensors.  (This Java version is for the Java weenies; normal people may prefer the python version.  I did it as an exercise to see if I remembered how to do stuff in Java.)

The sensor data is sent to mqtt by either zigbee2mqtt for sensors that use zigbee or sent to mqtt by the linux program rtl_433.

For zigbee you need a USB zigbeee dongle as well as the zigbee2mqtt software (available here on github). The zigbee2mqtt web site here on github lists the available USB zigbee dongles. The Electrolama zig-a-zig-ah! (zzh!) is well regarded by the author of zigbee2mqtt. The Conbee II has lots of stars and reviews on Amazon. One disadvantage with the zigbee sensors is that I know of only one that's rated for outdoor use, the Philips Hue outdoor motion sensor, which also has a temperature sensor. The zigbee sensors don't send out readings very often, perhaps because zigbee is strong on low power usage.

For the 433 MHz sensors you need a USB rtl sdr dongle. The one from rtl-sdr.com is a good choice as well as nooelec. For interpreting the data from the rtl sdr dongle I use the linux program rtl_433. It's a command line program, with options to send its output to mqtt. There are many 433 MHz temperature sensors available, sold as additional ones for an indoor/outdoor digital thermometer. I have an AccuRite sensor and a ThermoPro sensor. Also an ancient sensor for a Radio Shack indoor/outdoor thermometer. All three work with the rtl_433 software. That software probably works with every known sensor in the world. My rtl sdr dongle is also picking up the readings from some of the neighbors' sensors.

I have both dongles on my Linux Debian PC and for mqtt I'm using mosquitto. Mosquitto is getting sensor readings for both zigbee sensors as well as 433 MHz sensors. (A Raspberry Pi should work reasonably well.)

For the 433 MHz sensors you'll need to edit the file src/main/resources/mqtt.properties; in it is an assignment to mqtt.sensors. The format is one or more lines of
```
official name from rtl_433=your friendly name
```
with a comma between each. To figure out what model name to use I installed MQTT Explorer on my Windows PC. It's very handy for seeing what's being sent to your mqtt service.

For the zigbee sensor names you set them up with zigbee2mqtt. For me that's also running on my Linux PC. I configured it so that it has a web page I can use from my Windows PC (my Linux PC doesn't have a monitor or keyboard).

The mqtt.properties file is where you set the name/address for the mqtt server.  It also specifies the topics this program subscribes to.  You can also change the filename for the log file, which should normally only get lines when this program reconnects to mqtt, which for me happens after I've woken my PC when it was asleep.

So to summarize, my Linux PC is running mosquitto, zigbee2mqtt, and rtl_433. It has both a zigbee USB dongle and an rtl sdr dongle plugged into it. My Windows PC runs this python program to display the temperature readings. I start the program with temperature.cmd (in the linux folder) as one of my Windows startup programs.  Also in the linux folder are files for the rtl_433 program; rtl433.sh is the shell script that runs it, and rtl433.service is what I used to make it a systemd service.  I don't remember how I installed the service but rtl433.sh should be made executable with chmod +x and copied to the /usr/local/etc directory.  You may also need to go to the rtl_433 web site for some other stuff to get it to work; just installing it via apt-get may not do everything that's needed.  The first time I installed it I got the sources and compiled and installed it, following their instructions.