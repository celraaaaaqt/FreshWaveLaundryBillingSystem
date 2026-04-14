import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class LaundryBillingSystem {

	static Scanner sc = new Scanner(System.in);

	//this part acts as a storage of completed transactions
	static ArrayList<Transaction> transactions = new ArrayList<>();
	
	//static to allow us call these to functions or main class
	static int transactionCounter = 1001;
	static String USERNAME = "admin";
	static String PASSWORD = "1234";
	static boolean loggedIn = false;

	public static void main(String[] args) {

//function for login to appear first
		login();

//loop
		while (true) {

//main menu
			System.out.println("\n====== LAUNDRY BILLING SYSTEM ======");
			System.out.println("[1] New Transaction");
			System.out.println("[2] View Transactions");
			System.out.println("[3] Update Transaction");
			System.out.println("[4] Delete Transaction");
			System.out.println("[5] Logout");
			System.out.println("[6] Exit");

			System.out.print("Choose option: ");

//trim() to remove any extra spaces
			String input = sc.nextLine().trim();

//if the user just pressed the enter button
			if (input.isEmpty()) {
				System.out.println("Cannot be empty. Please enter a number.\n");
				continue;
			}

			int choice;
			
//string converted into int (for validation for numbers)
			try {
				choice = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.\n");
				continue;
			}

//call the functions in main menu
			switch (choice) {
			case 1:
				newTransaction();
				break;
			case 2:
				viewTransactions();
				break;
			case 3:
				viewTransactions();
				updateTransaction();
				break;
			case 4:
				viewTransactions();
				deleteTransaction();
				break;
			case 5:
				logout();
				login();
				break;
			case 6:
				System.out.println("System Closed.");
				return;
			default:
				System.out.println("Invalid choice. (1-6) only.\n");
			}
		}
	}

	static void newTransaction() {
		System.out.println("\n--- NEW TRANSACTION ---");

		String name;
		while (true) {
			System.out.print("Customer Name: ");
			name = sc.nextLine().trim();

			if (name.isEmpty()) {
				System.out.println("Name cannot be empty.\n");
			} else if (!name.matches("[a-zA-Z .'-]+")) {
				System.out.println("Invalid name. Letters only.");
			} else {
				break;
			}
		}

		String contact;

		while (true) {
			System.out.print("\nContact Number: ");
			contact = sc.nextLine();

			if (contact.isEmpty()) {
				System.out.println("Contact number cannot be empty, please try again.\n");
			} else if (contact.startsWith("09") && contact.matches("\\d{11}")) {
				break;
			} else {
				System.out.println("Must start at 09 and must 11 digits/numbers only.");
			}

		}
		int typeChoice;
		while (true) {
			System.out.println("\nLaundry Type:");
			System.out.println("[1] Wash & Fold (50 PHP / kg)");
			System.out.println("[2] Dry Clean (80 PHP / kg)");
			System.out.print("Choose laundry type (1-2): ");

			String input = sc.nextLine().trim();

			if (input.isEmpty()) {
				System.out.println("Input cannot be empty. Please enter a number.\n");
				continue;
			}

//string to double conversion same as string to int conversion up above
			try {
				typeChoice = Integer.parseInt(input);
				if (typeChoice < 1 || typeChoice > 2) {
					System.out.println("Invalid choice. Please select 1-2.\n");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Numbers only.");
			}
		}
		
		//string is empty because the values of it assigned to switch case
		String laundryType = "";
		
		//same here
		double pricePerKg = 0;

		switch (typeChoice) {
		case 1:
			laundryType = "Wash & Fold";
			pricePerKg = 50;
			break;
		case 2:
			laundryType = "Dry Clean";
			pricePerKg = 80;
			break;
		default:
			System.out.println("Invalid choice.");
			return;
		}
		
		double weight;
		while (true) {
			System.out.print("\nEnter weight (1kg - 20kg): ");
			String input = sc.nextLine().trim();

			if (input.isEmpty()) {
				System.out.println("Input cannot be empty. Please enter a number.");
				
				//to go back in enter weight section
				continue;
			}

			try {
				weight = Double.parseDouble(input);
				if (weight < 1 || weight > 20) {
					System.out.println("Weight is out of range (1kg - 20kg only). Please try again.");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Numbers only.\n");
			}
		}
		double baseTotal = weight * pricePerKg;

		int clothesChoice = 0;
		String clothesType = "";

		while (true) {
			System.out.println("\nClothes Type:");
			System.out.println("[1] White");
			System.out.println("[2] Colored");
			System.out.println("[3] White + Colored");
			System.out.print("Choose clothes type (1-3): ");

			String input = sc.nextLine().trim();

			if (input.isEmpty()) {
				System.out.println("Input cannot be empty. Please enter a number.");
				continue;
			}

			try {
				clothesChoice = Integer.parseInt(input);
				if (clothesChoice == 1) {
					clothesType = "White";
					break;
				} else if (clothesChoice == 2) {
					clothesType = "Colored";
					break;
				} else if (clothesChoice == 3) {
					clothesType = "White + Colored";
					break;
				} else {
					System.out.println("Invalid choice. Please select between (1-3) only.\n");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Numbers only.\n");
			}
		}

//assign as 0 because the value of it will depends on conditions 
		double ironingFee = 0;
		double expressFee = 0; 

//the condition (y/n)
		if (yesNo("\nAdd ironing (+30 Php)? ")) {
			ironingFee = 30;
		}
		if (yesNo("\nAdd express fee (+50 Php)? ")) {
			expressFee = 50;
		}

		double total = baseTotal + ironingFee + expressFee;

		double discount = 0;

		String promo;
        String appliedPromo = "";
		while (true) {
			System.out.println("\nPROMO CODES: ");
			System.out.println("- 10NTHFRESHWAVEANNIVERSARY (10% discount)");
			System.out.println("- FSstudent (50 Php discount)");
			System.out.println("- NEWCUSTOMER (5% discount)");
			System.out.print("Enter Promo Code (or press Enter to skip): ");
			
			promo = sc.nextLine();
           
			if (promo.isEmpty()) {
				break;

			} else if (promo.equals("10NTHFRESHWAVEANNIVERSARY")) {
				discount = total * 0.10;
				appliedPromo = promo;
				System.out.println("Promo Applied! 10% Discount.");
				break;
				
			} else if (promo.equals("FSstudent")) {
				discount = 50;
				appliedPromo = promo;
				System.out.println("Promo Applied! 50 Php Discount.");
				break;
				
			} else if (promo.equals("NEWCUSTOMER")) {
				discount = total * 0.05;
				appliedPromo = promo;
				System.out.println("Promo Applied! 5% Discount.");
				break;
				
			} else {
				System.out.println("Invalid promo code, please try again.\n");
			}
		}

		total = total - discount;
		if (total < 0) {
			total = 0;
		}

//datatype and variable for confirming/previewing transaction 
boolean confirmed = false;
while (!confirmed) {
    
    System.out.println("\n========== Confirm Details ==========");
    System.out.println("[1] Customer Name   : " + name);
    System.out.println("[2] Contact Number  : " + contact);
    System.out.println("[3] Laundry Type    : " + laundryType);
    System.out.println("[4] Clothes Type    : " + clothesType);
    System.out.println("[5] Weight          : " + weight + " kg");
    System.out.println("[6] Ironing Fee     : " + (ironingFee > 0 ? "+30 Php" : "None"));
    System.out.println("[7] Express Fee     : " + (expressFee > 0 ? "+50 Php" : "None"));
    System.out.println("[8] Promo Code      : " + (discount > 0 ? promo : "None"));
    System.out.println("=====================================");

    if (yesNo("\nIs all information correct? ")) {
        //if the user selects "y", the program will go on summary
        confirmed = true;
    } else {
        System.out.print("\nEnter number of field to edit (1-8): ");
        
        String editInput = sc.nextLine().trim();
        int editChoice;
        
        try {
            editChoice = Integer.parseInt(editInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            continue;
        }

        switch (editChoice) {
            case 1:
                while (true) {
                    System.out.print("\nNew Customer Name: ");
                    name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty.");
                    } else if (!name.matches("[a-zA-Z .'-]+")) {
                        System.out.println("Invalid name. Letters only.");
                    } else break;
                }
                break;

            case 2:
                while (true) {
                    System.out.print("\nNew Contact Number: ");
                    contact = sc.nextLine().trim();
                    if (contact.isEmpty()) {
                        System.out.println("Contact cannot be empty.");
                    } else if (contact.startsWith("09") && contact.matches("\\d{11}")) {
                        break;
                    } else {
                        System.out.println("Must start with 09 and be 11 digits.");
                    }
                }
                break;

            case 3:
                while (true) {
                    System.out.println("\n[1] Wash & Fold (50 PHP / kg)");
                    System.out.println("[2] Dry Clean (80 PHP / kg)");
                    System.out.print("Choose laundry type: ");
                    
                    String lt = sc.nextLine().trim();
                    
                    if (lt.equals("1")) { 
                    laundryType = "Wash & Fold"; 
                    pricePerKg = 50; 
                    break; 
                    
                    }else if (lt.equals("2")) {
                     laundryType = "Dry Clean";
                     pricePerKg = 80; 
                     break; 
                     
                    } else System.out.println("Invalid choice.");
                }
                
                baseTotal = weight * pricePerKg;
                break;

            case 4:
                while (true) {
                    System.out.println("\n[1] White");
					System.out.println("[2] Colored");
					System.out.println("[3] White + Colored");
                    System.out.print("Choose clothes type: ");
                    
                    String ct = sc.nextLine().trim();
                    
                    if (ct.equals("1")) { 
                    clothesType = "White"; 
                    break; 
                    
                    } else if (ct.equals("2")) { 
                    clothesType = "Colored"; 
                    break; 
                    
                    } else if (ct.equals("3")) {
                    clothesType = "White + Colored"; 
                    break; 
                    }
                    
                    else System.out.println("Invalid choice.");
                }
                break;

            case 5:
                while (true) {
                    System.out.print("\nNew Weight (kg): ");
                    String wInput = sc.nextLine().trim();
                    try {
                        weight = Double.parseDouble(wInput);
                        if (weight < 1 || weight > 20) System.out.println("Weight must be 1-20 kg.");
                        else { baseTotal = weight * pricePerKg; break; }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                break;

            case 6:
                ironingFee = yesNo("\nAdd ironing (+30 Php)? ") ? 30 : 0;
                break;

            case 7:
                expressFee = yesNo("\nAdd express fee (+50 Php)? ") ? 50 : 0;
                break;

            case 8:
            
                while (true) {
                    System.out.println("\nPROMO CODES: 10NTHFRESHWAVEANNIVERSARY / FSstudent / NEWCUSTOMER");
                    System.out.print("Enter Promo Code (or press Enter to skip): ");

                    String newPromo = sc.nextLine().trim();
                    double tempTotal = baseTotal + ironingFee + expressFee;
                    
                    if (newPromo.isEmpty()) { 
                    promo = "";
                    appliedPromo = "";
                    discount = 0;
					break; 
					}
					
					if (newPromo.equals(appliedPromo)){
						System.out.println("Promo Code is already used.");
						break;
					}
					else if (newPromo.equals("10NTHFRESHWAVEANNIVERSARY")) {
					promo = newPromo;
                    appliedPromo = newPromo;
					discount = tempTotal * 0.10; 
					System.out.println("10% Discount applied.");
					break; 
					}

                    else if (newPromo.equals("FSstudent")) {
					promo = newPromo;
                    appliedPromo = newPromo;
				    discount = 50; 
					System.out.println("50 Php Discount applied."); 
					break; 

				    } else if (newPromo.equals("NEWCUSTOMER")) {
					promo = newPromo;
                    appliedPromo = newPromo;
					discount = tempTotal * 0.05; 
					System.out.println("5% Discount applied."); 
					break; 
				    }

                    else {
					System.out.println("Invalid promo code.");
					}
                }
                break;

            default:
                System.out.println("Invalid field number. Choose 1-8.");
        }

        //recalculate totals after any edit
        baseTotal = weight * pricePerKg;
        double tempTotal = baseTotal + ironingFee + expressFee;
        if (discount > 0 && !promo.equalsIgnoreCase("FSstudent")) {
            if (promo.equalsIgnoreCase("10NTHFRESHWAVEANNIVERSARY")) discount = tempTotal * 0.10;
            else if (promo.equalsIgnoreCase("NEWCUSTOMER")) discount = tempTotal * 0.05;
        }
        total = tempTotal - discount;
        if (total < 0) total = 0;
    }
}



		System.out.println("\n========== Summary ==========");
		System.out.println("Customer Name       : "  + name);
		System.out.println("Contact Number      : "  + contact);
		System.out.println("Laundry Type        : " + laundryType);
		System.out.println("Clothes Type        : "  + clothesType);
		System.out.println("Subtotal            : " + weight + "kg x " + pricePerKg + " = " + baseTotal + " Php");

		if (ironingFee > 0) {
			System.out.println("Ironing Fee: +30 Php ");
		}

		if (expressFee > 0) {
			System.out.println("Express Fee: +50 Php");
		}

		System.out.println("Subtotal         : " + (baseTotal + ironingFee + expressFee) + " Php");

		System.out.println("Discount            : " + discount + " Php");
		System.out.printf("Grand Total : %.2f Php\n", total);

		double payment = 0;
		while (true) {
			System.out.print("Enter Payment	    : ");
			String input = sc.nextLine().trim();

			if (input.isEmpty()) {
				System.out.println("Input cannot be empty. Please enter a number.\n");
				continue;
			}

			try {
				payment = Double.parseDouble(input);
				if (payment <= 0) {
					System.out.println("Payment must be positive. Please try again.\n");
				} else if (payment < total) {
					System.out.println("Insufficient payment. Please try again.\n");
				} else if (payment > 10000) {
					System.out.println("Payment is out of range (max 10,000 Php). Please try again.\n");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.\n");
			}
		}
		double change = payment - total;
		System.out.printf("Change: %.2f Php\n", change);

		//to create new objects when creating transactions
		Transaction t = new Transaction(
			transactionCounter++,
			name,
			contact,
			laundryType,
			clothesType,
			weight,
			total,
			payment,
			change,
			"Pending",
			ironingFee,
			expressFee,
			discount);

		//to add on the arraylist for each transaction
		transactions.add(t);

		System.out.println("\nTransaction Successful!");
		printReceipt(t); //called function for printing the receipt
	}

	static void viewTransactions() {
		System.out.println("\n--- TRANSACTION LIST ---");

		if (transactions.isEmpty()) {
			System.out.println("No records found.");
			return;
		}

		//here is the transactions done, this allows us to view the newly created transactions, we used loop to allow us view all new transactions
		for (Transaction t : transactions) {
			System.out.println("ID: " + t.id +
							   " | Name: " + t.customerName +
							   " | Contact Number: " + t.contactNumber +
							   " | Wash Type: " + t.laundryType +
							   " | Clothes Type: " + t.clothesType +
							   " | Total: " + t.totalAmount +
							   " | Status: " + t.status + " | Date: " + t.date + "\n");
		}
	}

	static void updateTransaction() {
		int id = 0;
		while (true) {
			System.out.print("Enter Transaction ID to update (Or enter '0' to go back in main menu): ");
			String input = sc.nextLine().trim();

			if (input.isEmpty()) {
				System.out.println("Input cannot be empty. Please enter a number.\n");
				continue;
			}

			try {
				id = Integer.parseInt(input);
				if (id < 0) {
					System.out.println("Please enter a valid ID or 0 to go back.\n");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Numbers only.\n");
			}
		}

		if (id == 0) {
			return;
		}

		boolean found = false;

//to track the specific id in transactions arraylist, if the id matched the user's input, then it can be update'
    for (Transaction t : transactions) {
        if (t.id == id) {
            found = true;

            if (t.status.equalsIgnoreCase("Pending")) {
                if (yesNo("Update this transaction id " + "'" + t.id  + "'" + " to 'Ready'? ")) {
                    t.status = "Ready";
                    System.out.println("Transaction updated to Ready!");
                } else {
                    System.out.println("Status update canceled.");
                }

            } else if (t.status.equalsIgnoreCase("Ready")) {
                if (yesNo("Update this transaction to 'Claimed'? ")) {
                    t.status = "Claimed";
                    System.out.println("Transaction updated to Claimed!");
                } else {
                    System.out.println("Status update canceled.");
                }

            } else if (t.status.equalsIgnoreCase("Claimed")) {
                System.out.println("This transaction is already CLAIMED. No further updates allowed.");
            }

            break;
        }
    }

    if (!found) {
        System.out.println("Transaction ID " + id + " does not exist.");
    }
}

	static void deleteTransaction() {
		int id = 0;
		while (true) {
			System.out.print("Enter Transaction ID to delete (Or enter '0' to go back in main menu): ");
			String input = sc.nextLine().trim();

			if (input.isEmpty()) {
				System.out.println("Input cannot be empty. Please enter a number.\n");
				continue;
			}

			try {
				id = Integer.parseInt(input);
				if (id < 0) {
					System.out.println("Please enter a valid ID or 0 to go back.\n");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Numbers only.\n");
			}
		}

		if (id == 0) {
			return;
		}


		Iterator<Transaction> iterator = transactions.iterator();

		while (iterator.hasNext()) {
			Transaction t = iterator.next();
			if (t.id == id) {
				if (yesNo("Are you sure you want to delete? ")) {
					iterator.remove();
					System.out.println("Transaction Deleted.");
				} else {
					System.out.println("Deletion canceled.");
				}
				return;
			}
		}

		System.out.println("Transaction not found.");
	}

	static void printReceipt(Transaction t) {
		System.out.println("\n======= RECEIPT =======");
		System.out.println("\n---FRESHWAVE LAUNDRY SHOP SERVICES---\n");
		System.out.println("Transaction ID: " + t.id);
		System.out.println("Customer: " + t.customerName);
		System.out.println("Contact: " + t.contactNumber);
		System.out.println("Laundry Type: " + t.laundryType);
		System.out.println("Clothes Type: " + t.clothesType);
		System.out.println("Weight: " + t.weight + " kg");
		System.out.println("Total: " + t.totalAmount + " Php");
		System.out.println("Payment: " + t.payment + " Php");
		System.out.println("Change: " + t.change + " Php");
		System.out.println("Status: " + t.status);
		System.out.println("Date: " + t.date);
		System.out.println("========================\n");
	}

	static boolean yesNo(String message) {

		while (true) {
			System.out.print(message + "(Y/N): ");
			String input = sc.nextLine().toUpperCase();

			if (input.equals("Y"))
				return true;
			if (input.equals("N"))
				return false;

			System.out.println("Please enter Y or N only.");
		}
	}

	static int validator() {
		while (true) {
			if (sc.hasNextInt()) {
				return sc.nextInt();
			} else {
				System.out.print("Invalid input. Please enter a number: ");
				sc.next();
			}
		}
	}

	static void login() {
		while (!loggedIn) {
			System.out.println("====== LOGIN ======");

			System.out.print("Username: ");
			String user = sc.nextLine();

			System.out.print("Password: ");
			String pass = sc.nextLine();

			if (user.equals(USERNAME) && pass.equals(PASSWORD)) {
				loggedIn = true;
				System.out.println("Login Successful!");
			} else {
				System.out.println("Invalid username or password.\n");
			}
		}
	}

	static void logout() {

		if(yesNo("\nAre you  sure you want to logout?")){
		loggedIn = false;
		System.out.println("\nYou have been logged out.\n");
		} 
	}
}

class Transaction {
	//the datas of user's input will be gathered here'
	int id;
	String customerName;
	String contactNumber;
	String laundryType;
	String clothesType;
	double weight;
	double totalAmount;
	double payment;
	double change;
	String status;
	double ironingFee;
	double expressFee;
	double discount;
	String date;

	//we'll put in in the constructor'
	Transaction(int id, String customerName, String contactNumber,
				String laundryType, String clothesType, double weight,
				double totalAmount, double payment,
				double change, String status, double ironingFee, double expressFee, double discount) {

		//assign the variables using "this"
		this.id = id;
		this.customerName = customerName;
		this.contactNumber = contactNumber;
		this.laundryType = laundryType;
		this.clothesType = clothesType;
		this.weight = weight;
		this.totalAmount = totalAmount;
		this.payment = payment;
		this.change = change;
		this.status = status;
		this.ironingFee = ironingFee;
		this.expressFee = expressFee;
		this.discount = discount;
		
		//for exact time (ph time)
		ZoneId phZone = ZoneId.of("Asia/Manila");
		ZonedDateTime phTime = ZonedDateTime.now(phZone);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
		this.date = phTime.format(formatter);
	}
}
