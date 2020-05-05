package org.shreejalarammandir.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.shreejalarammandir.model.activity.Activity;

public class ActivityService {
	
	public static List getActiveActivities() {
		Session session = Connection.getConnection();
		Query query = session.createQuery("from Activity where active='true'");
		List<Activity> l = query.list();
		return l;
	}
	
	public static List getActiveActivitiesLatestSessions() {
		Session session = Connection.getConnection();
		Query query = session.createQuery("from Activity where active='true'");
		List<Activity> l = query.list();
		List<org.shreejalarammandir.model.activity.Session> sessions = new ArrayList<org.shreejalarammandir.model.activity.Session>();
		for (Activity a : l) {
			Hibernate.initialize(a.getSession());
			for (org.shreejalarammandir.model.activity.Session s : a.getSession()) {
				sessions.set(0, s);								// getting lastest session
			}
		}
		return sessions;
	}
}
