package illinois.sweng.sctracker.test;

import illinois.sweng.sctracker.DBAdapter;
import illinois.sweng.sctracker.SC2TrackerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;

public class DBAdapterTest extends
		ActivityInstrumentationTestCase2<SC2TrackerActivity> {

	private DBAdapter mDBAdapter;
	private Activity mActivity;
	private static final String DATABASE_NAME = "TrackerDatabase";
	private JSONObject playerData1, playerData2, teamData1, teamData2,
			eventData1, eventData2;

	public DBAdapterTest() {
		super("illinois.sweng.sctracker", SC2TrackerActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mActivity = getActivity();
		mActivity.deleteDatabase(DATABASE_NAME);
		mDBAdapter = new DBAdapter(mActivity);

		playerData1 = new JSONObject();
		playerData2 = new JSONObject();
		teamData1 = new JSONObject();
		teamData2 = new JSONObject();
		eventData1 = new JSONObject();
		eventData2 = new JSONObject();
		try {
			playerData1.put("picture", "picture");
			playerData1.put("handle", "handle");
			playerData1.put("name", "name");
			playerData1.put("race", "race");
			playerData1.put("team", 1);
			playerData1.put("nationality", "nationality");
			playerData1.put("elo", 10);
			playerData2.put("picture", "picture2");
			playerData2.put("handle", "handle2");
			playerData2.put("name", "name2");
			playerData2.put("race", "race2");
			playerData2.put("team", 1);
			playerData2.put("nationality", "nationality2");
			playerData2.put("elo", 100);

			teamData1.put("name", "Bad");
			teamData1.put("tag", "Terrible");
			teamData2.put("name", "Good");
			teamData2.put("tag", "Awesome");

			eventData1.put("picture", "picture");
			eventData1.put("name", "name");
			eventData1.put("start_date", "start");
			eventData1.put("end_date", "end");

			eventData2.put("picture", "picture2");
			eventData2.put("name", "name2");
			eventData2.put("start_date", "begin2");
			eventData2.put("end_date", "end2");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mDBAdapter.open();

	}

	@Override
	protected void tearDown() throws Exception {
		// mDBAdapter.close();
		super.tearDown();
		// mActivity.deleteDatabase(DATABASE_NAME);
	}

	public void testHasNoPlayer() {
		assertFalse(mDBAdapter.hasPlayer(0));
	}

	public void testInsertPlayer() {
		mDBAdapter.insertPlayer(1, playerData1);
		assertTrue(mDBAdapter.hasPlayer(1));
	}

	public void testGetPlayer() {
		mDBAdapter.insertPlayer(1, playerData1);
		Cursor firstCursor = mDBAdapter.getPlayer(1);
		assertTrue(firstCursor.getCount() == 1);
		firstCursor.moveToFirst();
		int index = firstCursor.getColumnIndex("name");
		String name = firstCursor.getString(index);
		assertEquals(name, "name");
		Cursor secondCursor = mDBAdapter.getPlayerByPK(1);
		assertTrue(secondCursor.getCount() == 1);
		secondCursor.moveToFirst();
		name = secondCursor.getString(index);
		assertEquals(name, "name");
	}

	public void testUpdatePlayer() {
		mDBAdapter.insertPlayer(1, playerData1);
		JSONObject newData = new JSONObject();
		try {
			newData.put("name", "new name");
			mDBAdapter.updatePlayer(1, newData);
			Cursor firstCursor = mDBAdapter.getPlayerByPK(1);
			firstCursor.moveToFirst();
			int index = firstCursor.getColumnIndex("name");
			String name = firstCursor.getString(index);
			assertEquals(name, "new name");
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testUpdatePlayerTable() {
		JSONArray playerArray = new JSONArray();
		JSONObject playerJSON1 = new JSONObject();
		JSONObject playerJSON2 = new JSONObject();
		try {
			playerJSON1.put("pk", 1);
			playerJSON1.put("fields", playerData1);
			playerJSON2.put("pk", 2);
			playerJSON2.put("fields", playerData2);
		} catch (JSONException e) {
			fail();
		}
		playerArray.put(playerJSON1);
		playerArray.put(playerJSON2);
		mDBAdapter.updatePlayerTable(playerArray);
		assertTrue(mDBAdapter.hasPlayer(1));
		assertTrue(mDBAdapter.hasPlayer(2));
	}

	public void testGetAllPlayers() {
		mDBAdapter.insertPlayer(1, playerData1);
		mDBAdapter.insertPlayer(2, playerData2);
		Cursor cursor = mDBAdapter.getAllPlayers();
		assertTrue(cursor.getCount() == 2);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("name");
		String name = cursor.getString(index);
		assertEquals("name", name);
	}

	public void testGetPlayersByTeam() {
		mDBAdapter.insertPlayer(1, playerData1);
		mDBAdapter.insertPlayer(2, playerData2);
		Cursor cursor = mDBAdapter.getPlayersByTeam(1);
		assertTrue(cursor.getCount() == 2);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("team");
		int team = cursor.getInt(index);
		assertEquals(1, team);
	}

	public void testHasNoTeam() {
		assertFalse(mDBAdapter.hasTeam(1));
	}

	public void testInsertTeam() {
		mDBAdapter.insertTeam(1, teamData1);
		assertTrue(mDBAdapter.hasTeam(1));
	}

	public void testGetTeam() {
		mDBAdapter.insertTeam(1, teamData1);
		Cursor cursor = mDBAdapter.getTeam(1);
		assertTrue(cursor.getCount() == 1);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("name");
		String teamName = cursor.getString(index);
		assertEquals(teamName, "Bad");
	}

	public void testGetTeamByPK() {
		mDBAdapter.insertTeam(1, teamData1);
		Cursor cursor = mDBAdapter.getTeamByPK(1);
		assertTrue(cursor.getCount() == 1);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("name");
		String teamName = cursor.getString(index);
		assertEquals(teamName, "Bad");

	}

	public void testGetAllTeams() {
		mDBAdapter.insertTeam(1, teamData1);
		mDBAdapter.insertTeam(2, teamData2);
		Cursor cursor = mDBAdapter.getAllTeams();
		assertTrue(cursor.getCount() == 2);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("name");
		String teamName = cursor.getString(index);
		assertEquals(teamName, "Bad");
		cursor.moveToNext();
		index = cursor.getColumnIndex("name");
		teamName = cursor.getString(index);
		assertEquals(teamName, "Good");

	}

	public void testUpdateTeam() {
		mDBAdapter.insertTeam(1, teamData1);
		mDBAdapter.updateTeam(1, teamData2);
		Cursor cursor = mDBAdapter.getTeamByPK(1);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("name");
		String teamName = cursor.getString(index);
		assertEquals(teamName, "Good");
	}

	public void testUpdateTeamTable() {
		JSONArray teamArray = new JSONArray();
		JSONObject teamJSON1 = new JSONObject();
		JSONObject teamJSON2 = new JSONObject();
		try {
			teamJSON1.put("pk", 1);
			teamJSON1.put("fields", teamData1);
			teamJSON2.put("pk", 2);
			teamJSON2.put("fields", teamData2);
		} catch (JSONException e) {
			fail();
		}
		teamArray.put(teamJSON1);
		teamArray.put(teamJSON2);
		mDBAdapter.updateTeamTable(teamArray);
		assertTrue(mDBAdapter.hasTeam(1));
		assertTrue(mDBAdapter.hasTeam(2));
	}

	public void testHasNoEvent() {
		assertFalse(mDBAdapter.hasEvent(1));
	}

	public void testInsertEvent() {
		mDBAdapter.insertEvent(1, eventData1);
		assertTrue(mDBAdapter.hasEvent(1));
	}

	public void testGetEvent() {
		mDBAdapter.insertEvent(1, eventData1);
		mDBAdapter.insertEvent(2, eventData2);
		Cursor cursor = mDBAdapter.getEvent(1);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("name");
		String name = cursor.getString(index);
		assertEquals(name, "name");

		cursor = mDBAdapter.getEventByPK(1);
		cursor.moveToFirst();
		name = cursor.getString(index);
		assertEquals(name, "name");

	}

	public void testGetAllEvents() {
		mDBAdapter.insertEvent(1, eventData1);
		mDBAdapter.insertEvent(2, eventData2);
		Cursor cursor = mDBAdapter.getAllEvents();
		assertTrue(cursor.getCount() == 2);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("name");
		String name = cursor.getString(index);
		assertEquals(name, "name");
		cursor.moveToNext();
		index = cursor.getColumnIndex("name");
		name = cursor.getString(index);
		assertEquals(name, "name2");

	}

	public void testUpdateEvents() {
		mDBAdapter.insertEvent(1, eventData1);
		mDBAdapter.updateEvent(1, eventData2);
		Cursor cursor = mDBAdapter.getEventByPK(1);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex("name");
		String name = cursor.getString(index);
		assertEquals(name, "name2");
	}

	public void testUpdateEventTable() {
		JSONArray eventArray = new JSONArray();
		try{
			eventData1.put("pk", 1);
			eventData2.put("pk", 2);
		} catch (JSONException e) {
			fail();
		}
		eventArray.put(eventData1);
		eventArray.put(eventData2);
		mDBAdapter.updateEventTable(eventArray);
		assertTrue(mDBAdapter.hasEvent(1));
		assertTrue(mDBAdapter.hasEvent(2));
	}
}
