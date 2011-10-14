package se.chalmers.doit.test.data.storage;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;

import se.chalmers.doit.core.ITask;
import se.chalmers.doit.core.ITaskCollection;
import se.chalmers.doit.core.implementation.Task;
import se.chalmers.doit.core.implementation.TaskCollection;
import se.chalmers.doit.data.storage.implementation.DataCache;
import android.test.AndroidTestCase;

/**
 * Test class for testing DataCache.java
 * @author Kaufmann
 *
 */
public class DataCacheTest extends AndroidTestCase {

	private DataCache cache;

	@Override
	@Before
	public void setUp() throws Exception {
		cache = new DataCache();
	}

	public void testAddList() {
		assertTrue(cache.getAllLists().size() == 0);
		TaskCollection t1 = new TaskCollection("TaskCol1", new ArrayList<ITask>());
		TaskCollection t2 = new TaskCollection("TaskCol2", new ArrayList<ITask>());
		TaskCollection t3 = new TaskCollection("TaskCol3", new ArrayList<ITask>());
		assertTrue(cache.addList(t1));
		assertTrue(cache.addList(t2));
		assertTrue(cache.addList(t3));
		assertTrue(cache.getAllLists().size() == 3);
		assertTrue(cache.getAllLists().contains(t1));
		assertTrue(cache.getAllLists().contains(t2));
		assertTrue(cache.getAllLists().contains(t3));
	}

	public void testAddLists() {
		assertTrue(cache.getAllLists().size() == 0);
		TaskCollection t1 = new TaskCollection("TaskCol1", new ArrayList<ITask>());
		TaskCollection t2 = new TaskCollection("TaskCol2", new ArrayList<ITask>());
		TaskCollection t3 = new TaskCollection("TaskCol3", new ArrayList<ITask>());
		ArrayList<ITaskCollection> colList = new ArrayList<ITaskCollection>();
		assertTrue(colList.add(t1));
		assertTrue(colList.add(t2));
		assertTrue(colList.add(t3));
		assertTrue(cache.addLists(colList) == 3);
		assertTrue(cache.addLists(colList) == 0);
		assertTrue(cache.getAllLists().size() == 3);
		assertTrue(cache.getAllLists().contains(t1));
		assertTrue(cache.getAllLists().contains(t2));
		assertTrue(cache.getAllLists().contains(t3));
	}

	public void testAddTask() {
		ITask task = new Task("Name", "Description", false);
		ITask task2 = new Task("Name2", "Description2", true);
		Collection<ITask> taskList = new ArrayList<ITask>();
		taskList.add(task);
		ITaskCollection col = new TaskCollection("Collection", taskList);
		assertTrue(cache.getAllTasks().size() == 0);
		assertTrue(cache.addTask(task, col));
		assertFalse(cache.addTask(task, col));
		assertTrue(cache.getAllTasks().size() == 1);
		assertTrue(cache.addTask(task2, col));
		assertTrue(cache.getAllTasks().size() == 2);
		assertTrue(cache.getAllTasks().contains(task));
		assertTrue(cache.getAllTasks().contains(task2));
	}

	public void testAddTasks() {
		ITask task = new Task("Name", "Description", false);
		ITask task2 = new Task("Name2", "Description2", true);
		Collection<ITask> taskList = new ArrayList<ITask>();
		taskList.add(task);
		taskList.add(task2);
		ITaskCollection col = new TaskCollection("Collection", new ArrayList<ITask>());
		assertTrue(cache.getAllTasks().size() == 0);
		assertTrue(cache.addTasks(taskList, col) == 2);
		assertTrue(cache.addTasks(taskList, col) == 0);
		assertTrue(cache.getAllTasks().size() == 2);
		assertTrue(cache.getAllTasks().contains(task));
		assertTrue(cache.getAllTasks().contains(task2));
	}

	public void testClearData() {
		ITask task = new Task("Name", "Description", false);
		ITask task2 = new Task("Name2", "Description2", true);
		Collection<ITask> taskList = new ArrayList<ITask>();
		taskList.add(task);
		taskList.add(task2);
		ITaskCollection col = new TaskCollection("Collection", new ArrayList<ITask>());
		cache.addTasks(taskList, col);
		assertTrue(cache.getAllLists().size() != 0);
		assertTrue(cache.getAllTasks().size() != 0);
		cache.clearData();
		assertTrue(cache.getAllLists().size() == 0);
		assertTrue(cache.getAllTasks().size() == 0);
	}

	public void testEditList() {
		TaskCollection tc1 = new TaskCollection("Name");
		TaskCollection tc2 = new TaskCollection("Name2");
		cache.addList(tc1);
		assertTrue(cache.getAllLists().contains(tc1));
		cache.editList(tc1, tc2);
		assertTrue(cache.getAllLists().contains(tc2));
	}

	public void testEditTask() {
		ITask task = new Task("Name", "Description", false);
		ITask task2 = new Task("Name2", "Description2", true);
		TaskCollection tc1 = new TaskCollection("Name");
		cache.addTask(task, tc1);
		assertTrue(cache.getAllTasks().contains(task));
		assertFalse(cache.getAllTasks().contains(task2));
		cache.editTask(task, task2);
		assertFalse(cache.getAllTasks().contains(task));
		assertTrue(cache.getAllTasks().contains(task2));
	}

	public void testGetAllLists() {
		assertTrue(cache.getAllLists().size() == 0);
		cache.addList(new TaskCollection("Name1"));
		cache.addList(new TaskCollection("Name2"));
		cache.addList(new TaskCollection("Name3"));
		assertTrue(cache.getAllLists().size() == 3);
	}

	public void testGetAllTasks() {
		assertTrue(cache.getAllTasks().size() == 0);
		TaskCollection tc = new TaskCollection("TC");
		cache.addTask(new Task("Task", "Krabba", false), tc);
		cache.addTask(new Task("Task2", "Krabba2", true), tc);
		cache.addTask(new Task("Task3", "Krabba3", false), tc);
		assertTrue(cache.getAllTasks().size() == 3);
	}

	public void testMoveTask() {
		TaskCollection tc = new TaskCollection("TC");
		TaskCollection tc2 = new TaskCollection("TC2");
		Task t = new Task("Task", "Krabba", false);
		cache.addTask(t, tc);
		cache.addList(tc2);
		cache.moveTask(t, tc2);
		for(ITaskCollection c : cache.getAllLists()){
			if(c.getName().equals("TC")){
				assertFalse(c.getTasks().contains(t));
			}
			if(c.getName().equals("TC2")){
				assertTrue(c.getTasks().contains(t));
			}
		}
	}

	public void testRemoveList() {
		TaskCollection tc1 = new TaskCollection("tc1");
		TaskCollection tc2 = new TaskCollection("tc2");
		TaskCollection tc3 = new TaskCollection("tc3");
		TaskCollection tc4 = new TaskCollection("tc4");

		cache.addList(tc1);
		cache.addList(tc2);
		cache.addList(tc3);
		cache.addList(tc4);

		cache.removeList(tc2);

		assertTrue(cache.getAllLists().contains(tc1));
		assertFalse(cache.getAllLists().contains(tc2));
		assertTrue(cache.getAllLists().contains(tc3));
		assertTrue(cache.getAllLists().contains(tc4));
	}

	public void testRemoveLists() {
		TaskCollection tc1 = new TaskCollection("tc1");
		TaskCollection tc2 = new TaskCollection("tc2");
		TaskCollection tc3 = new TaskCollection("tc3");
		TaskCollection tc4 = new TaskCollection("tc4");

		cache.addList(tc1);
		cache.addList(tc2);
		cache.addList(tc3);
		cache.addList(tc4);

		ArrayList<ITaskCollection> list = new ArrayList<ITaskCollection>();
		list.add(tc2);
		list.add(tc4);

		cache.removeLists(list);

		assertTrue(cache.getAllLists().contains(tc1));
		assertFalse(cache.getAllLists().contains(tc2));
		assertTrue(cache.getAllLists().contains(tc3));
		assertFalse(cache.getAllLists().contains(tc4));
	}

	public void testRemoveTask() {
		Task t1 = new Task("t1", "blubb", false);
		Task t2 = new Task("t2", "blubb", false);
		Task t3 = new Task("t3", "blubb", false);
		Task t4 = new Task("t4", "blubb", false);

		TaskCollection col = new TaskCollection("Default");

		cache.addList(col);
		cache.addTask(t1, col);
		cache.addTask(t2, col);
		cache.addTask(t3, col);
		cache.addTask(t4, col);

		cache.removeTask(t2);

		assertTrue(cache.getAllTasks().contains(t1));
		assertFalse(cache.getAllTasks().contains(t2));
		assertTrue(cache.getAllTasks().contains(t3));
		assertTrue(cache.getAllTasks().contains(t4));
	}

	public void testRemoveTasks() {
		Task t1 = new Task("t1", "blubb", false);
		Task t2 = new Task("t2", "blubb", false);
		Task t3 = new Task("t3", "blubb", false);
		Task t4 = new Task("t4", "blubb", false);

		TaskCollection col = new TaskCollection("Default");

		cache.addList(col);
		cache.addTask(t1, col);
		cache.addTask(t2, col);
		cache.addTask(t3, col);
		cache.addTask(t4, col);

		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(t2);
		list.add(t4);
		cache.removeTasks(list);

		assertTrue(cache.getAllTasks().contains(t1));
		assertFalse(cache.getAllTasks().contains(t2));
		assertTrue(cache.getAllTasks().contains(t3));
		assertFalse(cache.getAllTasks().contains(t4));
	}
}