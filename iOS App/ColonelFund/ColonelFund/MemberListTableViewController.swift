//
//  MemberListTableViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class MemberListTableViewController: UITableViewController {
    
    //MARK: Properties
    //dummy array
    var memberList = [String]()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        memberList.append("John Smith")
        memberList.append("Liam Gallagher")
        memberList.append("Noel Gallagher")
        memberList.append("John Lennon")
        memberList.append("Paul McCartney")
        memberList.append("Don Henley")
        memberList.append("Phil Collins")
        memberList.append("Jimmy Paige")
        memberList.append("Trevor Hurst")
        memberList.append("Adam Levine")
        memberList.append("Axl Rose")
        memberList.append("Chad Kroeger")
        memberList.append("Dave Grohl")
        memberList.append("Jimi Hendrix")
        memberList.append("Kurt Cobain")
        
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return memberList.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "MemberListTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? MemberListTableViewCell  else {
            fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
        }
        
        let member = memberList[indexPath.row]
        cell.nameLabel.text = member
        cell.usernameLabel.text = "johnwsmith" //replace with username
        //TODO: - if no profile pic, use placeholder
        placeholderProfilePic(nameObj: cell.nameLabel.text!, imageObj: cell.profilePicImageView)
        
        
        cell.layoutIfNeeded()

        return cell
    }
    
    func placeholderProfilePic(nameObj: String, imageObj: UIImageView) {
        let placeholder = UILabel()
        placeholder.frame.size = CGSize(width: 50.0, height: 50.0)
        placeholder.textColor = UIColor.white
        placeholder.font = UIFont.boldSystemFont(ofSize: 26)
        let name = nameObj.split(separator: " ", maxSplits: 1).map(String.init)
        var firstName = name[0]
        var lastName = name[1]
        placeholder.text = String(firstName[firstName.startIndex]) + String(lastName[lastName.startIndex])
        placeholder.textAlignment = NSTextAlignment.center
        placeholder.backgroundColor = UIColor.darkGray
        placeholder.layer.cornerRadius = 25.0
        placeholder.layer.masksToBounds = true
        
        UIGraphicsBeginImageContext(placeholder.frame.size)
        placeholder.layer.render(in: UIGraphicsGetCurrentContext()!)
        imageObj.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowMember":
            guard let memberViewController = segue.destination as? ViewMemberViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            guard let selectedMemberCell = sender as? MemberListTableViewCell else {
                fatalError("Unexpected sender: \(sender)")
            }
            
            guard let indexPath = tableView.indexPath(for: selectedMemberCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            let selectedMember = memberList[indexPath.row]
            memberViewController.tempNameText = selectedMember
            
        default:
            fatalError("Unexpected Segue Identifier: \(segue.identifier)")
        }
    }
    

}
