import java.util.Scanner;

/**
 * The Driver class contains an “infinite” loop that continuously prompts the user for the next command and reacts to what they type. It also initialized and construct the world 
 * @author Anh Vu, Duong Pham, Devin Best, Evan Lambert
 * @version October 2024
 * @version November 2024
 */

public class Driver {
    public static Location currLocation;
    public static ContainerItem myInventory; 

    public static void main(String[] args) {
        ///Scanner that reads its data from the standard input stream
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to The House");

        createWorld();

        ///An “infinite” loop that continuously prompts the user for the next command and reacts to what they type
        while (true) {
            
            System.out.print("Enter command: ");

            String command = scanner.nextLine();
            String[] splttedCommand = command.split(" ");

            switch (splttedCommand[0].toLowerCase()) {
                case (""):
                    break;
                case ("look"):
                    System.out.print(currLocation.getLocName());
                    System.out.print(" - ");
                    System.out.println(currLocation.getLocDescription());
                    for (int i = 0; i < currLocation.numItems(); i += 1) {
                        System.out.print("+ ");
                        System.out.println(currLocation.getItem(i).getName());
                    }
                    break;
                case ("examine"):
                    if (splttedCommand.length != 2) {
                        System.out.println("Please specify the item you want to examine");
                    }
                    else {
                        Item itemFound = currLocation.getItem(splttedCommand[1]);
                        if (itemFound == null) {
                            System.out.println("Cannot find that item");
                        }
                        else if(itemFound instanceof ContainerItem)
                        {
                            ContainerItem containerItem = (ContainerItem) itemFound;
                            System.out.println(containerItem.toString());
                        }
                        else {
                            System.out.println(itemFound.toString());
                        }
                    }
                    break;
                case("go"):
                    if (splttedCommand.length != 2) {
                        System.out.println("Please specify the direction you want to go");
                        break;
                    }
                    if(currLocation.canMove(splttedCommand[1].toLowerCase())) { ///Need rewrite
                        currLocation = currLocation.getLocation(splttedCommand[1].toLowerCase());  ///Need rewrite
                    }
                    else {
                        System.out.println("Cannot go that way");
                    }
                    break;
                case("inventory"):
                    ///Print list of item (names-only) that are currently stored in the character's inventory
                    System.out.println(myInventory.toString());;
                    break;
                case("take"):
                    ///Try to find the matching item at the current location, if found, remove from location and add to inventory, if not print "Cannot find that item here" 
                    if (splttedCommand.length == 2) {
                        Item itemFound = currLocation.getItem(splttedCommand[1]);
                        if (itemFound == null) {
                            System.out.println("Cannot find that item here");
                        }
                        else {
                            currLocation.removeItem(splttedCommand[1]);
                            myInventory.addItem(itemFound);
                        }
                    }
                    else if (splttedCommand.length > 2) {
                        if (splttedCommand[2].equals("from")) {
                            ContainerItem containerFound =  null;
                            
                            if(currLocation.getItem(splttedCommand[3]) instanceof ContainerItem) {
                                containerFound = (ContainerItem) currLocation.getItem(splttedCommand[3]);
                            }
                            
                            if (containerFound == null) {
                                System.out.println("Cannot find that container here, or mentioned item is not a container");
                                break;
                            }

                            boolean itemFound = containerFound.hasItem(splttedCommand[1]);
                            if (!itemFound) {
                                System.out.println("Cannot find that item in this container");
                            }
                            else {
                                Item itemRemoved = containerFound.removeItem(splttedCommand[1]);
                                myInventory.addItem(itemRemoved);
                            }
                        }
                        else {
                            System.out.println("Invalid command format. Use 'take <item>' or 'take <item> from <container>");
                        }
                    }
                    else {
                        System.out.println("Please specify the item you want to take");
                    }
                    break;
                case("drop"):
                    ///Try to find the matching item in the character's inventory, remove it and add to the current location
                    if (splttedCommand.length != 2) {
                        System.out.println("Please specify the item you want to drop");
                    }
                    else {
                        boolean itemFound = myInventory.hasItem(splttedCommand[1]);
                        if (!itemFound) {
                            System.out.println("Cannot find that item in your inventory");
                        }
                        else {
                            Item itemRemoved = myInventory.removeItem(splttedCommand[1]);
                            currLocation.addItem(itemRemoved);
                        }
                    }
                    break;
                case ("put"):
                    if (splttedCommand.length == 4) {
                        if (splttedCommand[2].equals("in")) {
                            ContainerItem containerFound = null;
                            if(currLocation.getItem(splttedCommand[3]) instanceof ContainerItem) {
                                containerFound = (ContainerItem) currLocation.getItem(splttedCommand[3]);
                            }

                            if (containerFound == null) {
                                System.out.println("Cannot find that container here, or mentioned item is not a container");
                                break;
                            }

                            boolean itemFound = myInventory.hasItem(splttedCommand[1]);
                            if (!itemFound) {
                                System.out.println("Cannot find that item in your inventory");
                            }
                            else {
                                Item itemRemoved = myInventory.removeItem(splttedCommand[1]);
                                containerFound.addItem(itemRemoved);
                            }
                        }
                        else {
                            System.out.println("Invalid command format. Use 'put <item> in <container>'");
                        }
                    }
                    else {
                        System.out.println("Please specify what item and where you want to put it");
                    }
                    break;
                case("help"):
                    ///Print all the commands currently supported with a one-sentence description
                    System.out.println(commandDes("look", "Display location's name, description and item available there"));
                    System.out.println(commandDes("inventory", "Inspect your inventory"));
                    System.out.println(commandDes("examine <item>", "Display item's types and description"));
                    System.out.println(commandDes("go <direction>", "Go in the direction (north, south, east, west) that you wanted"));
                    System.out.println(commandDes("take <item>", "Add an item that is available in your current location to your inventory"));
                    System.out.println(commandDes("take <item>  from <container>", "Add an item from a container to your inventory"));
                    System.out.println(commandDes("drop <item>", "Drop an item from you inventory"));
                    System.out.println(commandDes("put <item> in <container>", "Put an item that is in your inventory in a container at your current location"));
                    System.out.println(commandDes("quit", "Exit the game"));
                    break;
                case ("quit"):
                    System.exit(0);
                    break;
                default: 
                    System.out.println("Unknown command, type 'help' for all available commands");
            }
        }
    }

    public static String commandDes(String command, String description) {
        return new StringBuilder()
        .append(command)
        .append(" - ")
        .append(description)
        .toString();
    }

    public static void createWorld() {
        myInventory = new ContainerItem("Inventory", "Toolbox", "This is your inventory, which stores the items you feel that can be useful");

        ///Create and initialize "Kitchen" location
        Location kitchen = new Location("Kitchen", "A dark kitchen whose lights are flickering");
        Location hallway = new Location("Hallway", "A long line");
        Location bedroom = new Location("Bedroom", "A place for rest");
        Location livingroom = new Location("Livingroom", "Not a living room");

        currLocation = kitchen;

        Item knifeItem = new Item("Knife", "Tool", "Dismantle and Cleave, with a huge red stain on it, likely blood");
        Item turkeyItem = new Item("Turkey", "Food", "Some leftover turkey, smells terrible, likely rotten");
        Item lambItem = new Item("Lamb", "Decoration", "A broken old lamb");
        Item breadItem = new Item("Bread", "Food", "It's bread");
        Item tablItem = new Item("Table", "Decoration", "An old wooden table, one of its leg is missing");
        Item carpetItem = new Item("Carpet", "Decoration", "A moldy carpet with weird stains on it");
        Item boxItem = new Item("Box", "Box", "A tighly locked silver box, look relatively new compare to everything else");
        
        ContainerItem desk = new ContainerItem("Desk", "Decoration", "The one below all");
        desk.addItem(new Item("Pen", "Tool", "A thing that makes you ace Chad's exams."));
        desk.addItem(new Item("Notebook", "Tool", "A notebook that can be written."));

        ContainerItem chest = new ContainerItem("Chest", "Tool", "A thing that store another thing");
        chest.addItem(new Item("Coin", "Treasure", "A shiny gold coin."));
        chest.addItem(new Item("Map", "Tool", "A tattered map of the surrounding area."));

        ContainerItem vault = new ContainerItem("Vault", "Tool", "A safe that store valuable things");
        vault.addItem(new Item("Diamond", "Gem", "A thing that can make you rich."));
        vault.addItem(new Item("Key", "Tool", "A thing that lead to another thing."));

        kitchen.addItem(knifeItem);
        kitchen.addItem(turkeyItem);
        kitchen.addItem(desk);
        kitchen.addItem(breadItem);
        bedroom.addItem(lambItem);
        bedroom.addItem(boxItem);
        bedroom.addItem(vault);
        livingroom.addItem(tablItem);
        livingroom.addItem(chest);
        hallway.addItem(carpetItem);

        kitchen.addItem(boxItem);

        kitchen.connect("west", bedroom);
        bedroom.connect("east", kitchen);

        bedroom.connect("north", hallway);
        hallway.connect("south", bedroom);

        hallway.connect("east", livingroom);
        livingroom.connect("west", hallway);



        livingroom.connect("south", kitchen);
        kitchen.connect("north", livingroom);
    }
}