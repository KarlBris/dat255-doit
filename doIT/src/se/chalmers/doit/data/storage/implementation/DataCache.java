package se.chalmers.doit.data.storage.implementation;

import java.util.*;

import se.chalmers.doit.core.*;
import se.chalmers.doit.core.implementation.TaskCollection;
import se.chalmers.doit.data.storage.IDataCache;

/**
 * Class storing cached data and delegating datachanges to ... TODO: Describe
 * better...
 * 
 * @author Kaufmann
 */

// TODO Add connections to DB

public class DataCache implements IDataCache {

	// TODO: Add hash maps for ID's :)
	private final Collection<ITaskCollection> lists = new ArrayList<ITaskCollection>();

	@Override
	public boolean addList(final ITaskCollection collection) {
		return _addList(collection);
	}

	@Override
	public int addLists(final Collection<ITaskCollection> collection) {
		int nAdded = 0;
		for (ITaskCollection t : collection) {
			if (_addList(t)) {
				nAdded++;
			}
		}
		return nAdded;
	}

	@Override
	public boolean addTask(final ITask task, final ITaskCollection collection) {
		return _addTask(task, collection);
	}

	@Override
	public int addTasks(final Collection<ITask> tasks,
			final ITaskCollection collection) {
		int nTasks = 0;
		for (ITask t : tasks) {
			if (_addTask(t, collection)) {
				nTasks++;
			}
		}
		return nTasks;
	}

	@Override
	public void clearData() {
		lists.clear();
	}

	@Override
	public boolean editList(final ITaskCollection oldCollection,
			final ITaskCollection newCollection) {
		return _editList(oldCollection, newCollection);
	}

	@Override
	public boolean editTask(final ITask oldTask, final ITask newTask) {
		ITaskCollection tc = null;
		for (ITaskCollection c : lists) {
			if (c.getTasks().contains(oldTask)) {
				tc = c;
				break;
			}
		}

		if (tc != null) {
			return (_removeTask(oldTask) && _addTask(newTask, tc));
		}
		return false;
	}

	@Override
	public Collection<ITaskCollection> getAllLists() {
		Collection<ITaskCollection> ret = new ArrayList<ITaskCollection>();
		for (ITaskCollection w : lists) {
			ret.add(w);
		}
		return ret;
	}

	@Override
	public Collection<ITask> getAllTasks() {
		Collection<ITask> ret = new ArrayList<ITask>();
		for (ITaskCollection w : lists) {
			ret.addAll(w.getTasks());
		}
		return ret;
	}

	@Override
	public boolean moveTask(final ITask task, final ITaskCollection collection) {
		return (_removeTask(task) && _addTask(task, collection));
	}

	@Override
	public boolean removeList(final ITaskCollection collection) {
		return _removeList(collection);
	}

	@Override
	public int removeLists(final Collection<ITaskCollection> collection) {
		int nLists = 0;
		for (ITaskCollection c : collection) {
			if (_removeList(c)) {
				nLists++;
			}
		}
		return nLists;
	}

	@Override
	public boolean removeTask(final ITask task) {
		return _removeTask(task);
	}

	@Override
	public int removeTasks(final Collection<ITask> listOfTasksToRemove) {
		int nTasks = 0;
		for (ITask t : listOfTasksToRemove) {
			if (_removeTask(t)) {
				nTasks++;
			}
		}
		return nTasks;
	}

	private boolean _addList(final ITaskCollection col) {
		if (_listExists(col)) {
			return lists.add(col);
		}
		return false;
	}

	private boolean _addTask(final ITask task, final ITaskCollection collection) {
		if (!_taskExists(task)) {
			Collection<ITask> oldTasks = collection.getTasks();
			oldTasks.add(task);
			String oldName = collection.getName();
			if(!_listExists(collection) || _removeList(collection)){
				return _addList(new TaskCollection(oldName, oldTasks));
			}
		}
		return false;
	}

	private boolean _editList(final ITaskCollection oc, final ITaskCollection nc) {
		return (_removeList(oc) && _addList(nc));
	}

	private boolean _removeList(final ITaskCollection collection) {
		return lists.remove(collection);
	}

	private boolean _removeTask(final ITask task) {
		for (ITaskCollection c : lists) {
			if (c.getTasks().contains(task)) {
				Collection<ITask> col = c.getTasks();
				col.remove(task);
				return _editList(c, new TaskCollection(c.getName(), col));
			}
		}
		return false;
	}

	private boolean _taskExists(final ITask task) {
		for (ITaskCollection c : lists) {
			if (c.getTasks().contains(task)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean _listExists(final ITaskCollection col) {
		return lists.contains(col);
	}
}