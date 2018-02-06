import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class zkBankWatcher implements Watcher{
	private static final int SESSION_TIMEOUT = 5000;

	private static String rootMembers = "/members";
	private static String aMember = "/member-";
	private static String rootOperations = "/operations";
	private String myId;
	public static String server;

	zkBank bank = new zkBank();
	
	// This is static. A list of zookeeper can be provided for decide where to connect
	String[] hosts = {"127.0.0.1:2181", "127.0.0.1:2182", "127.0.0.1:2183"};

	private ZooKeeper zk;

	public zkBankWatcher () {

		// Select a random zookeeper server
		Random rand = new Random();
		int i = rand.nextInt(hosts.length);

		// Create a session and wait until it is created.
		// When is created, the watcher is notified
		try {
			if (zk == null) {
				zk = new ZooKeeper(hosts[i], SESSION_TIMEOUT, cWatcher);
				try {
					// Wait for creating the session. Use the object lock
					wait();
					//zk.exists("/",false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (Exception e) {
			System.out.println("Error");
		}
		
        server = hosts[i];
		System.out.println("You are connected to server: " + server );
		// Add the process to the members in zookeeper

		if (zk != null) {
			try {			
				// Create root member folder, if not already created
				String rootMembersCreated; 
				Stat rootMembersExist = zk.exists(rootMembers, watcherMember); //this);
				if (rootMembersExist == null) {
					rootMembersCreated = zk.create(rootMembers, new byte[0],
							Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					System.out.println("Root member folder created: " + rootMembersCreated);
				}

				// Create a znode for new joining member
				myId = zk.create(rootMembers + aMember, new byte[0],
						Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

				myId = myId.replace(rootMembers + "/", "");
				System.out.println("My member ID: "+ myId );

				List<String> listMember = zk.getChildren(rootMembers, watcherMember, rootMembersExist); //this, s);
				
				// Create root operation folder, if not already created
				String rootOperationsCreated;
				Stat rootOperationsExist = zk.exists(rootOperations, watcherOperation); //this);
				if (rootOperationsExist == null) {
					rootOperationsCreated = zk.create(rootOperations, new byte[0],
							Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					System.out.println("Root operation folder created: " + rootOperationsCreated);
				}
				
				List<String> listOperation = zk.getChildren(rootOperations, watcherOperation, rootOperationsExist); //this, s);
				
			} catch (KeeperException e) {
				System.out.println("The session with Zookeeper failes. Closing");
				return;
			} catch (InterruptedException e) {
				System.out.println("InterruptedException raised");
			}
		}
	}

	private Watcher cWatcher = new Watcher() {
		public void process (WatchedEvent e) {
			System.out.println("Session created");
			System.out.println(e.toString());
			notify();
		}
	};

	private Watcher  watcherMember = new Watcher() {
		public void process(WatchedEvent event) {
			System.out.println("------------------Watcher Member------------------\n");
			try {
				System.out.println("Event: " + event.toString());		
				List<String> list = zk.getChildren(rootMembers,  watcherMember); //this);
				printListMembers(list);
				Collections.sort(list);
				String newMember = list.get(list.size()-1);
				System.out.println("New member: "+newMember);

			} catch (Exception e) {
				System.out.println("Exception: wacherMember");
			}
		}
	};
	
	private Watcher  watcherOperation = new Watcher() {
		public void process(WatchedEvent event) {
			System.out.println("------------------Watcher Operation------------------\n");
			try {
				System.out.println("Event: " + event.toString());
				List<String> list = zk.getChildren(rootOperations,  watcherOperation); //this);
				if (list.size() != 0) {
					printListOperations(list);
					Collections.sort(list);
					String newOp = list.get(list.size()-1);
					System.out.println("New operation: "+ newOp);
					byte[] data = zk.getData("/operations/" + newOp, false, null);
					zkOperationBank operation = (zkOperationBank) bank.sendMessages.convertToObject(data);
					//String str = new String(bn, "UTF-8");	
					System.out.println("operation: "+ operation);				
					bank.handleReceiverMsg(operation);
				}
			
			} catch (Exception e) {
				System.out.println("Exception: wacherOperation");
			}
		}
	};

	public void process(WatchedEvent event) {
		try {
			System.out.println("!!!!!!" + event.toString());
			List<String> listMember = zk.getChildren(rootMembers, watcherMember); //this);
			printListMembers(listMember);
			List<String> listOperation = zk.getChildren(rootOperations, watcherOperation); //this);
			printListMembers(listOperation);
		} catch (Exception e) {
			System.out.println("Error in project");
		}
	}

	private void printListMembers (List<String> list) {
		System.out.println("Number of members:" + list.size());
		if (list.size() != 0) {
		System.out.print("list of members: ");
		Collections.sort(list);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.print(string + ", ");			
		}
		}
		System.out.println();
	}
	
	private void printListOperations (List<String> list) {
		System.out.println("Number of Operations:" + list.size());
		if (list.size() != 0) {
		System.out.print("List of Operations: ");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.print(string + ", ");
		}
		}
		System.out.println();
		
	}

	public static synchronized void main(String[] args) throws KeeperException, InterruptedException {
		zkBankWatcher bankWatcher = new zkBankWatcher();
		ZooKeeper zk = bankWatcher.zk;
		zkBank bank = bankWatcher.bank;
		zkMainBank mainBank = new zkMainBank(zk, bank);
		    
		try {
			Thread.sleep(300000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
