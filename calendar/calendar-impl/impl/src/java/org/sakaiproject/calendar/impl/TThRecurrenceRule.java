/************************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 * @author mustansar@rice.edu
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.calendar.impl;

import java.util.GregorianCalendar;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Vector;
import java.util.List;

import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.api.TimeRange;
import org.sakaiproject.time.cover.TimeService;
import org.sakaiproject.time.api.TimeBreakdown;

import org.sakaiproject.util.CalendarUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
* <p> TTh RecurrenceRule is a time range generating rule that is based on a weekly recurrence. repeating Tuesdays and Thursdays</p>
* <p>The recurrences happen on the same day-of-week, at the same time as the prototype.</p>
* <p>TODO: support changing the day-of-week of recurrences Mustansar</p>
*/
public class TThRecurrenceRule extends RecurrenceRuleBase
{
	/** The unique type / short frequency description. */
	protected final static String FREQ = "TTh";
	private CalendarUtil calUtil = null;
	/**
	* Construct.
	*/
	public TThRecurrenceRule()
	{
		super();
		calUtil = new CalendarUtil();
	}	// TThRecurrenceRule
	/**
	* Construct with no  limits.
	* @param interval Every this many number of weeks: 1 would be weekly.
	*/
	public TThRecurrenceRule(int interval)
	{
		super(interval);
		calUtil = new CalendarUtil();
	}	// TThRecurrenceRule
	/**
	* Construct with count limit.
	* @param interval Every this many number of weeks: 1 would be weekly.
	* @param count For this many occurrences - if 0, does not limit.
	*/
	public TThRecurrenceRule(int interval, int count)
	{
		super(interval, count);
		calUtil = new CalendarUtil();
	}	// TThRecurrenceRule
	/**
	* Construct with time limit.
	* @param interval Every this many number of weeks: 1 would be weekly.
	* @param until No time ranges past this time are generated - if null, does not limit.
	*/
	public TThRecurrenceRule(int interval, Time until)
	{
		super(interval, until);
		calUtil = new CalendarUtil();
	}	// TThRecurrenceRule
	/**
	* Serialize the resource into XML, adding an element to the doc under the top of the stack element.
	* @param doc The DOM doc to contain the XML (or null for a string return).
	* @param stack The DOM elements, the top of which is the containing element of the new "resource" element.
	* @return The newly added element.
	*/
	public Element toXml(Document doc, Stack stack)
	{
		// add the "rule" element to the stack'ed element
		Element rule = doc.createElement("rule");
		((Element)stack.peek()).appendChild(rule);
		// set the class name - old style for CHEF 1.2.10 compatibility
		rule.setAttribute("class", "org.chefproject.osid.calendar.TThRecurrenceRule");
		// set the rule class name w/o package, for modern usage
		rule.setAttribute("name", "TThRecurrenceRule");
		// Do the base class part.
		setBaseClassXML(rule);
		return rule;
	}	// toXml
	/* (non-Javadoc)
	 * @see org.chefproject.service.calendar.RecurrenceRuleBase#getRecurrenceType()
	 */
	protected int getRecurrenceType()
	{
		return GregorianCalendar.WEEK_OF_MONTH;	
	}
   
	/**
	 * {@inheritDoc}
	 */
	public String getFrequencyDescription()
	{
		return rb.getFormattedMessage("set.TTh.fm", calUtil.getDayOfWeekName(2), calUtil.getDayOfWeekName(4));
	}
   
	/**
	 * {@inheritDoc}
	 */
	public String getFrequency()
	{
		return FREQ;
	}
	
	/**
	* Return a List of all RecurrenceInstance objects generated by this rule within the given time range, based on the
	* prototype first range, in time order.
	* @param prototype The prototype first TimeRange.
	* @param range A time range to limit the generated ranges.
	* @param timeZone The time zone to use for displaying times.
	* %%% Note: this is currently not implemented, and always uses the "local" zone.
	* @return a List of RecurrenceInstance generated by this rule in this range.
	*/
	public List generateInstances(TimeRange prototype, TimeRange range, TimeZone timeZone)
	{
		TimeBreakdown startBreakdown = prototype.firstTime().breakdownLocal();			
		List rv = new Vector();
		GregorianCalendar startCalendarDate = TimeService.getCalendar(TimeService.getLocalTimeZone(),0,0,0,0,0,0,0);	
		startCalendarDate.set(
									 startBreakdown.getYear(),
									 startBreakdown.getMonth() - 1, 
									 startBreakdown.getDay(), 
									 startBreakdown.getHour(),	
									 startBreakdown.getMin(), 
									 startBreakdown.getSec());	 //may have to move this line ahead 
		
		GregorianCalendar nextCalendarDate = (GregorianCalendar) startCalendarDate.clone();	
		
		//if day of week is not Tuesday or Thursday
		if( ((startCalendarDate.get(GregorianCalendar.DAY_OF_WEEK)!=3) &&
										((startCalendarDate.get(GregorianCalendar.DAY_OF_WEEK))!=5 )) )
		{
			//if day of week is Sunday, add two to make it Tuesday
			if (startCalendarDate.get(GregorianCalendar.DAY_OF_WEEK)==1){
				startCalendarDate.add(java.util.Calendar.DAY_OF_MONTH, 2);
			}
			//if day of week is Monday, add one to make it Tuesday
			else if (startCalendarDate.get(GregorianCalendar.DAY_OF_WEEK)==2){
				startCalendarDate.add(java.util.Calendar.DAY_OF_MONTH, 1);
			}
			//if day of week is Wednesday, add one to make it Thursday
			else if (startCalendarDate.get(GregorianCalendar.DAY_OF_WEEK)==4){
				startCalendarDate.add(java.util.Calendar.DAY_OF_MONTH, 1);
				
			} 
			//if day of week is Friday, add four to make it next Tuesday
			else if (startCalendarDate.get(GregorianCalendar.DAY_OF_WEEK)==6){
				startCalendarDate.add(java.util.Calendar.DAY_OF_MONTH, 4);
			}
			//must be Saturday, add three to make it next Tuesday
			else {
				startCalendarDate.add(java.util.Calendar.DAY_OF_MONTH, 3);			 
			} 
		}
		// end of else shifting startdate for TTh		
		
		nextCalendarDate = (GregorianCalendar) startCalendarDate.clone();
		int currentCount = 1;
		int hitCount=1;		//counts tth/mwf occurences when getCount()>0
		do
		{	
			Time nextTime = TimeService.newTime(nextCalendarDate);
			// is this past count?
			if ((getCount() > 0) && (hitCount > getCount()))
				break; 
			// is this past until?
			if ((getUntil() != null) && isAfter(nextTime, getUntil()) )
				break;
			
			TimeRange nextTimeRange = TimeService.newTimeRange(nextTime.getTime(), prototype.duration());
			
			// Is this out of the range?
			if (isOverlap(range, nextTimeRange))
			{
				TimeRange eventTimeRange = null;
				
				// Single time cases require special handling.
				if ( prototype.isSingleTime() )
				{
					eventTimeRange = TimeService.newTimeRange(nextTimeRange.firstTime());
				}
				else
				{
					eventTimeRange = TimeService.newTimeRange(nextTimeRange.firstTime(), nextTimeRange.lastTime(), true, false);
				}
				
				// use this one
				rv.add(new RecurrenceInstance(eventTimeRange, currentCount));
			}
			
			// if next starts after the range, stop generating I have added an extra condition here to test TTH/MWF Number of times repetition 
			else if (isAfter(nextTime, range.lastTime()) )
         { 
				break;
			}
         
			// Deal with finding respective Tuesdays and Thursdays
			do
			{
				nextCalendarDate = (GregorianCalendar) startCalendarDate.clone();//this line seems pointless 
				nextCalendarDate.add(java.util.Calendar.DAY_OF_MONTH, currentCount); //"1" is the supposed recurrence Type 
								
				int weekDay=nextCalendarDate.get(GregorianCalendar.DAY_OF_WEEK);
				if((getInterval()>1&&(weekDay==6)))
				{	
					nextCalendarDate.add(java.util.Calendar.DAY_OF_MONTH, ((getInterval()-1)*7)+currentCount+2);//"1" is the supposed recurrence Type
					currentCount+=((getInterval()-1)*7)+2;
				}
				else 
				{
					nextCalendarDate.add(java.util.Calendar.DAY_OF_MONTH, 1); //"1" is the supposed recurrence Type
					currentCount++;
				}
			} while(((nextCalendarDate.get(GregorianCalendar.DAY_OF_WEEK)!=3)&&((nextCalendarDate.get(GregorianCalendar.DAY_OF_WEEK))!=5 )));
			
			hitCount++;
		} while (true);
		
		return rv;
	}
}	// TThRecurrenceRule



