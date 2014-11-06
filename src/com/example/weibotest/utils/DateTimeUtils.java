package com.example.weibotest.utils;

import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

import android.text.format.DateFormat;

/**
 * 时间日期及格式化工具
 * 
 * @author abreal
 * 
 */
public class DateTimeUtils {

	private static final StringBuilder m_sbFormator = new StringBuilder();
	private static final Formatter m_formatter = new Formatter(m_sbFormator,
			Locale.getDefault());
	private static final Calendar m_calCurrent = Calendar.getInstance();
	private static final Calendar m_calInput = Calendar.getInstance();

	/**
	 * 毫秒数转换为时间格式化字符串
	 * 
	 * @param timeMs
	 * @return
	 */
	public static String stringForTime(long timeMs) {
		return stringForTime(timeMs, false);
	}

	/**
	 * 毫秒数转换为时间格式化字符串 支持是否显示小时
	 * 
	 * @param timeMs
	 * @return
	 */
	public static String stringForTime(long timeMs, boolean existsHours) {
		boolean bNegative = timeMs < 0;// 是否为负�?
		if (bNegative) {
			timeMs = -timeMs;
		}
		int totalSeconds = (int) (timeMs / 1000);

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		m_sbFormator.setLength(0);
		if (hours > 0 || existsHours) {
			return m_formatter.format("%s%02d:%02d:%02d", bNegative ? "-" : "",
					hours, minutes, seconds).toString();
		} else {
			return m_formatter.format("%s%02d:%02d", bNegative ? "-" : "",
					minutes, seconds).toString();
		}
	}

	/**
	 * 获取相差天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int daysBetween(final Calendar startDate,
			final Calendar endDate) {
		final boolean forward = startDate.before(endDate);
		final int multiplier = forward ? 1 : -1;
		final Calendar date = (Calendar) startDate.clone();
		int daysBetween = 0;
		int fieldAccuracy = 4;
		int field;
		int dayBefore, dayAfter;

		while (forward && date.before(endDate) || !forward
				&& endDate.before(date)) {
			switch (fieldAccuracy) {
			case 4:
				field = Calendar.MILLISECOND;
				break;
			case 3:
				field = Calendar.SECOND;
				break;
			case 2:
				field = Calendar.MINUTE;
				break;
			case 1:
				field = Calendar.HOUR_OF_DAY;
				break;
			default:
			case 0:
				field = Calendar.DAY_OF_MONTH;
				break;
			}
			dayBefore = date.get(Calendar.DAY_OF_MONTH);
			date.add(field, multiplier);
			dayAfter = date.get(Calendar.DAY_OF_MONTH);

			if (dayBefore == dayAfter && date.get(field) == endDate.get(field))
				fieldAccuracy--;
			if (dayBefore != dayAfter) {
				daysBetween += multiplier;
			}
		}
		return daysBetween;
	}

	private final static long HOURS_PER_DAY = 60 * 60 * 1000;

	/**
	 * 获取相差小时或分�?
	 * 
	 * @param startDate
	 * @param endDate
	 * @param hours
	 * @return
	 */
	public static int hoursOrMinutesBetween(final Calendar startDate,
			final Calendar endDate, boolean hours) {
		long beginMS = startDate.getTimeInMillis();
		long endMS = endDate.getTimeInMillis();
		long diff = (endMS - beginMS)
				/ (hours ? HOURS_PER_DAY : (HOURS_PER_DAY / 60));
		return (int) diff;
	}

	/**
	 * 获取格化化后时间
	 * 
	 * @param lTime
	 * @return
	 */
	public static String getFormatTime(long lTime) {
		if (lTime <= 0) {
			return "";
		}
		try {
			String strResult = "";
			m_calInput.setTimeInMillis(lTime);
			m_calCurrent.setTimeInMillis(System.currentTimeMillis());

			m_calCurrent.add(Calendar.DAY_OF_YEAR, -3); // 三天�?
			if (m_calCurrent.after(m_calInput)) {
				strResult = DateFormat.format("yyyy-MM-dd", m_calInput)
						.toString();
			} else {
				m_calCurrent.add(Calendar.DAY_OF_YEAR, 3);
				int nDistanceDay = daysBetween(m_calInput, m_calCurrent); // 与今天相距的天数
				if (nDistanceDay >= 3) {
					strResult = "三天前";
				} else if (nDistanceDay == 2) {
					strResult = "前天";
				} else if (nDistanceDay == 1
						|| (nDistanceDay == 0 && m_calInput
								.get(Calendar.DAY_OF_YEAR) != m_calCurrent
								.get(Calendar.DAY_OF_YEAR))) { // 相距不超�?4小时，但不是同一天时，也认定为昨�?
					strResult = "昨天";
				}
				strResult += DateFormat.format(" kk:mm", m_calInput).toString();
			}
			return strResult;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
}
