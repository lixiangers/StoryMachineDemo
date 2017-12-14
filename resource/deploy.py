#!/usr/bin/python
# -*- coding: UTF-8 -*-
# 文件名：eploy.py


import string
import os, sys, commands
import re
import tempfile
import shutil
import time

apks_path = "./apk/"


def exeCmd(cmd):
    print cmd
    os.system(cmd)


def pushData(mode):
    if mode == "old":
        exeCmd("adb push data/sdcard/ros/. /sdcard/ros/")
    else:
        exeCmd("adb push data/sdcard/. /sdcard/")

def installApk(type):
    type_prefix = "-release"
    if type == "debug":
        type_prefix = "-debug"
    path = apks_path
    for file in os.listdir(path):
        if file.find(".apk") > 0 and file.find(type_prefix) > 0:
            exeCmd("adb install -r -d " + path + file)

def installPlatformApk():
    path = "./"
    for file in os.listdir(path):
        if file.find(".apk") > 0 :
            exeCmd("adb install -r -d " + path + file)

if len(sys.argv) <= 2:
    print ("USAGE: python deploy <debug|release> <new|old>")
    sys.exit()

build_type = sys.argv[1]
mode = sys.argv[2]
print "MISSION START"

pushData(mode)
installPlatformApk()
installApk(build_type)
if mode == "new":
    exeCmd("adb reboot")
print "MISSION COMPLETE"
