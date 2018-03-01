# Sample script to make an announcement when a PM4 section changes state.
# This assumes that the PM4 sections are doing # short protection.  If they are doing
# auto-reversing, you might want to change the announcement message below.
#
# Also, it assumes that only one PM4 is going to trip at a time.  Note that the previous
# state (oldStateN variables) is not kept per-board, but just as one single copy.

# If you want to track changes in multiple boards, this needs to become a more complicated
# data structure that's e.g. indexed by board.  Also note that if multiple  sections change
# at once and you're using the "speak" command, the announcements may overlap.
#
# Author: Bob Jacobsen, copyright 2008
# Part of the JMRI distribution

import jmri

import java

import javax.swing

# LocoNet PM4 message is (hex)

# 0xD0 <ARG1>,<ARG2>,<ARG3>,<ARG4>, <CKSUM>

# <ARG1> = 0x60 + (5 upper bits of section address)

# <ARG2> = zone*2 + 16*(3 lower bits of section address)
# <ARG3> = 0x30 (or 0x10?) 

# define a listener to find PM4 messages,

# decode them and make an announcement

class PM4LnListener (jmri.jmrix.loconet.LocoNetListener) :
	
    def init(self) :
	
        # set initial states used to find changes

        self.oldState1 = False

        self.oldState2 = False

        self.oldState3 = False

        self.oldState4 = False

        return


    def message(self, msg) :
              # got a LocoNet message, see if from PM4

        if ( (msg.getElement(0)==0xD0) and ((msg.getElement(1)&0x60)==0x60) ) :

            # It's a PM4 message, decode contents

            board = (msg.getElement(1)&0x1)*128+(msg.getElement(2) &0x7F)+1

            section1 = ( (msg.getElement(4)&0x1) != 0)

            section2 = ( (msg.getElement(4)&0x2) != 0)
       	    section3 = ( (msg.getElement(4)&0x4) != 0)

            section4 = ( (msg.getElement(4)&0x8) != 0)

            # check each section for changing, and if so deal with it

            if (section1 != self.oldState1) :

                self.notify(board, 1, section1)

                self.oldState1 = section1

            if (section2 != self.oldState2) :

               	self.notify(board, 2, section2)

                self.oldState2 = section2
            if (section3 != self.oldState3) :

                self.notify(board, 3, section3)

                self.oldState3 = section3

            if (section4 != self.oldState4) :

                self.notify(board, 4, section4)

                self.oldState4 = section4
				

        return



    def notify(self, board, section, state) :
	
		
        # invoked when a change is seen, this does any

        # desired announcing

        if (state) :

            status = "shorted"

        else :

            status = "OK"

       	announce = "PM4 "+str(board)+" section "+str(section)+" is "+status

        print announce
	from time import sleep
	sleep(3)
	
        # You can also stop espeak by commenting the next line. This line is for Windows Systems,
		# other Operating Systems may or maynot require the full path. Simply delete the C:\Program Files\command_line\ 
		# portion of the statement if your not running Windows.

        java.lang.Runtime.getRuntime().exec(["c:\Program Files\eSpeak\command_line\eSpeak.exe", announce])
	
	return        

        # For more info on the speak command,see http://espeak.sf.net/        

        
       

# attach it   

l = PM4LnListener()

l.init()

jmri.jmrix.loconet.LnTrafficController.instance().addLocoNetListener(0xFF, l)


# The next part formats and sends a test LocoNet message.

# Its commented out during normal operation
#	board = 3
#	zone = 1
#	on = 1
#packet = jmri.jmrix.loconet.LocoNetMessage(6)
#packet.setElement(0, 0xD0)
#packet.setElement(1, 0x72+(((board-1)/128)&0x1))
#packet.setElement(2, ((board-1)&0x7F))
#packet.setElement(3, 0x3F)  
#F in lower bits means all AR
#packet.setElement(4, 0x10+(on << zone)) 
#set the control bit for just the channel
#jmri.jmrix.loconet.LnTrafficController.instance().sendLocoNetMessage(packet)

#     About this script - Jim Thompson

# Changed Speak to eSpeak as that is the executable in the current download 
# Added a delay after the eSpeak command to minimize the overlap of the
# announcements.  Quick shorts, such as rerailing an engine cleared so fast that the messages of the
# short and of its being cleared overlapped.  Never did get the test LocoNet message portion to work, but I 
# tested on the layout so I got real shorts and that made it easy to understand what was happening.
# To see the messages generated by PM4Announce, you must open the Script Output Window,
# PanelPro - Select Tools  -  Script Outut  Handy when your testing, not so much if your running an
# Operating Session.  
# To run this script  - PanelPro  - Select Tools  -  Run Script     

#     How to set it up - Jim Thompson

# Step one is to download and install eSpeak. http://espeak.sf.net/
# If you take the default install options, then the command 
# line I inserted in the script will work for a Windows environment.
# Step two is to set up PanelPro to run the Script everytime you start your Panel.
# On PanelPro or Decoder Pro Screen  Select Edit - Preferences
# Once the screen opens click the Show Advanced Preferences box
# Scroll down to the run scripts at startup section - Click Add Script - scroll to where your
# scripts are stored - On windows normally c:\Program Files\JMRI\Jython\your script name here.
# click open and your script will now appear on the preferences screen. When you restart PanelPro
# or DecoderPro this scrpt will load with your panel. 


