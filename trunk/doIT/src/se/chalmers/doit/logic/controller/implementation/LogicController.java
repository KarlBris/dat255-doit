package se.chalmers.doit.logic.controller.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import se.chalmers.doit.core.IStatisticalData;
import se.chalmers.doit.core.ITask;
import se.chalmers.doit.core.ITaskCollection;
import se.chalmers.doit.core.implementation.Task;
import se.chalmers.doit.data.storage.IDataStorage;
import se.chalmers.doit.data.storage.IStatisticsDataStorage;
import se.chalmers.doit.data.storage.implementation.DataCache;
import se.chalmers.doit.data.storage.implementation.StatisticsDataCache;
import se.chalmers.doit.logic.controller.ILogicController;
import se.chalmers.doit.logic.verification.IDataVerifier;
import se.chalmers.doit.logic.verification.implementation.DataVerifier;

public final class LogicController implements ILogicController {

	private final IDataVerifier verifier;
	private final IDataStorage data;
	private final IStatisticsDataStorage statistics;
	private static LogicController instance;

	private LogicController() {
		verifier = new DataVerifier();
		statistics = new StatisticsDataCache();
		data = new DataCache();
	}

	@Override
	public boolean addList(final ITaskCollection taskCollection) {
		if (data.getAllLists().size() == 0) {
			addNumberOfCreatedLists(1);
			return data.addList(taskCollection);
		}
		for (final ITaskCollection list : data.getAllLists()) {
			if (!list.getName().equals(taskCollection.getName())) {
				addNumberOfCreatedLists(1);
				return data.addList(taskCollection);
			}
		}
		return false;
	}

	@Override
	public int addLists(final Collection<ITaskCollection> collection) {
		if (data.getAllLists().size() == 0) {
			return data.addLists(collection);
		}
		for (final ITaskCollection savedList : data.getAllLists()) {
			for (final ITaskCollection newList : collection) {
				if (savedList.getName().equals(newList.getName())) {
					return 0;
				}
			}
		}
		addNumberOfCreatedLists(collection.size());
		return data.addLists(collection);
	}

	@Override
	public boolean addTask(final ITask task, final ITaskCollection collection) {
		if (verifier.verifyTask(task)) {
			addNumberOfCreatedTasks(1);
			return data.addTask(task, collection);
		}
		return false;
	}

	@Override
	public int addTasks(final Collection<ITask> tasks,
			final ITaskCollection collection) {
		for (final ITask task : tasks) {
			if (!verifier.verifyTask(task)) {
				return 0;
			}
		}
		addNumberOfCreatedTasks(tasks.size());
		return data.addTasks(tasks, collection);
	}

	@Override
	public void clearData() {
		data.clearData();
	}

	@Override
	public boolean completeTask(final ITask task) {
		return _completeTask(task);
	}

	@Override
	public boolean decompleteTask(final ITask task) {
		return _decompleteTask(task);
	}

	@Override
	public boolean editList(final ITaskCollection oldCollection,
			final ITaskCollection newCollection) {
		for (final ITaskCollection list : data.getAllLists()) {
			if (!newCollection.getName().equals(list.getName())) {
				return data.editList(oldCollection, newCollection);
			}
		}
		return false;
	}

	@Override
	public boolean editTask(final ITask oldTask, final ITask newTask) {
		if (verifier.verifyTask(newTask)) {
			return data.editTask(oldTask, newTask);
		}
		return false;
	}

	@Override
	public boolean editTasks(final ITaskCollection oldCollection,
			final ITaskCollection newCollection) {
		for (final ITask task : newCollection.getTasks()) {
			if (!verifier.verifyTask(task)) {
				return false;
			}
		}
		// TODO implement a method editTasks in DataCache!?
		data.editList(oldCollection, newCollection);
		return true;
	}

	@Override
	public Collection<ITaskCollection> getAllLists() {
		return data.getAllLists();
	}

	@Override
	public Collection<ITask> getAllTasks() {
		return data.getAllTasks();
	}

	public static synchronized ILogicController getInstance() {
		if (instance == null) {
			instance = new LogicController();
		}
		return instance;
	}

	@Override
	public boolean moveTask(final ITask task,
			final ITaskCollection taskCollection) {
		return data.moveTask(task, taskCollection);
	}

	@Override
	public boolean removeList(final ITaskCollection collection) {
		addNumberOfDeletedLists(1);
		return data.removeList(collection);
	}

	@Override
	public int removeLists(final Collection<ITaskCollection> collection) {
		addNumberOfDeletedLists(collection.size());
		return data.removeLists(collection);
	}

	@Override
	public boolean removeTask(final ITask task) {
		addNumberOfDeletedTasks(1);
		return data.removeTask(task);
	}

	@Override
	public int removeTasks(final Collection<ITask> listOfTasksToRemove) {
		addNumberOfDeletedTasks(listOfTasksToRemove.size());
		return data.removeTasks(listOfTasksToRemove);
	}

	@Override
	public int getNumberOfCreatedTasks(final int pastDays) {
		int retVal = 0;
		for (final IStatisticalData d : _getDataForInterval(pastDays)) {
			retVal += d.getCreatedTasks();
		}

		return retVal;
	}

	@Override
	public int getNumberOfFinishedTasks(final int pastDays) {
		int retVal = 0;
		for (final IStatisticalData d : _getDataForInterval(pastDays)) {
			retVal += d.getFinishedTasks();
		}

		return retVal;
	}

	@Override
	public int getNumberOfOverdueTasks(final int pastDays) {
		int retVal = 0;
		for (final IStatisticalData d : _getDataForInterval(pastDays)) {
			retVal += d.getOverdueTasks();
		}

		return retVal;
	}

	@Override
	public int getNumberOfDeletedTasks(final int pastDays) {
		int retVal = 0;
		for (final IStatisticalData d : _getDataForInterval(pastDays)) {
			retVal += d.getDeletedTasks();
		}

		return retVal;
	}

	@Override
	public int getNumberOfCreatedLists(final int pastDays) {
		int retVal = 0;
		for (final IStatisticalData d : _getDataForInterval(pastDays)) {
			retVal += d.getCreatedLists();
		}

		return retVal;
	}

	@Override
	public int getNumberOfDeletedLists(final int pastDays) {
		int retVal = 0;
		for (final IStatisticalData d : _getDataForInterval(pastDays)) {
			retVal += d.getDeletedLists();
		}

		return retVal;
	}

	@Override
	public void addNumberOfCreatedTasks(final int amount) {
		statistics.reportCreatedTasks(amount, new Date());

	}

	@Override
	public void addNumberOfFinishedTasks(final int amount) {
		statistics.reportFinishedTasks(amount, new Date());

	}

	@Override
	public void addNumberOfOverdueTasks(final int amount) {
		statistics.reportOverdueTasks(amount, new Date());

	}

	@Override
	public void addNumberOfDeletedTasks(final int amount) {
		statistics.reportDeletedTasks(amount, new Date());

	}

	@Override
	public void addNumberOfCreatedLists(final int amount) {
		statistics.reportCreatedLists(amount, new Date());

	}

	@Override
	public void addNumberOfDeletedLists(final int amount) {
		statistics.reportDeletedLists(amount, new Date());

	}

	@Override
	public void clearStatisticsData() {
		statistics.clearData();

	}

	@Override
	public boolean toggleTaskCompleted(final ITask task) {
		if (task.isCompleted()) {
			return _decompleteTask(task);
		}
		return _completeTask(task);
	}

	private Collection<IStatisticalData> _getDataForInterval(final int interval) {

		final Collection<IStatisticalData> retList = new ArrayList<IStatisticalData>();

		final Date tempDate = new Date();

		tempDate.setSeconds(0);
		tempDate.setMinutes(0);
		tempDate.setHours(0);

		Date date = new Date(tempDate.getTime() - (long) 86400000
				* (long) interval);

		if (interval < 0) {
			date = new Date(0);
		}

		for (final IStatisticalData d : statistics.getStatisticsData()) {
			if (d.getDate().compareTo(date) > 0) {
				retList.add(d);
			}
		}

		return retList;

	}

	private boolean _completeTask(final ITask task) {
		addNumberOfFinishedTasks(1);
		return data.editTask(task, new Task(task, true));
	}

	private boolean _decompleteTask(final ITask task) {
		addNumberOfFinishedTasks(-1);
		return data.editTask(task, new Task(task, false));
	}

}
