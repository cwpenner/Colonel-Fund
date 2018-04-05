//
//  MemberListTableViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class MemberListTableViewController: UITableViewController, MemberCollectionProtocol {
    
    //MemberCollectionProtocol
    //This has a MemberCollection delegate reload the table when the data is finished being loaded
    func memberDataDownloaded() {
        if self.refresher.isRefreshing
        {
            self.refresher.endRefreshing()
        }
        self.memberListTableView.reloadData()
    }
    
    //MARK: Properties
    @IBOutlet var memberListTableView: UITableView!
    var refresher: UIRefreshControl!
    let searchController = UISearchController(searchResultsController: nil)
    var filteredMembers = [Member]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        MemberCollection.sharedInstance.delegate = self
        MemberCollection.sharedInstance.updateFromRemote()
        
        //Pull to Refresh
        self.refresher = UIRefreshControl()
        self.refresher?.attributedTitle = NSAttributedString(string: "Pull to refresh")
        self.refresher?.addTarget(self, action: #selector(self.refreshMemberList(_:)), for: UIControlEvents.valueChanged)
        self.memberListTableView?.addSubview(refresher!)
        
        //Search Controller
        searchController.searchResultsUpdater = self
        searchController.obscuresBackgroundDuringPresentation = false
        searchController.searchBar.placeholder = "Search Events"
        navigationItem.searchController = searchController
        definesPresentationContext = true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc private func refreshMemberList(_ sender: Any) {
        MemberCollection.sharedInstance.updateFromRemote()
    }
    
    
    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if isFiltering() {
            return filteredMembers.count
        }
        
        return MemberCollection.sharedInstance.memberArray.count
    }

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 50
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "MemberListTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? MemberListTableViewCell  else {
            fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
        }
        
        let member : Member
        if isFiltering() {
            member = filteredMembers[indexPath.row]
        } else {
            member = MemberCollection.sharedInstance.memberArray[indexPath.row]
        }
        
        cell.nameLabel.text = member.getFormattedFullName()
        cell.usernameLabel.text = member.getEmailAddress()
        if member.getProfilePicURL().isEmpty {
            placeholderProfilePic(member: member, imageObj: cell.profilePicImageView)
        } else {
            loadProfilePicFromURL(url: member.getProfilePicURL(), imageObj: cell.profilePicImageView)
        }
        
        return cell
    }
    
    func placeholderProfilePic(member: Member, imageObj: UIImageView) {
        let placeholder = UILabel()
        placeholder.frame.size = CGSize(width: 42.0, height: 42.0)
        placeholder.textColor = UIColor.white
        placeholder.font = UIFont.boldSystemFont(ofSize: 20)
        placeholder.text = String(describing: member.getFirstName().first!) + String(describing: member.getLastName().first!)
        placeholder.textAlignment = NSTextAlignment.center
        placeholder.backgroundColor = UIColor.darkGray
        placeholder.layer.cornerRadius = 21.0
        placeholder.layer.masksToBounds = true
        
        UIGraphicsBeginImageContext(placeholder.frame.size)
        placeholder.layer.render(in: UIGraphicsGetCurrentContext()!)
        imageObj.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    
    func loadProfilePicFromURL(url: String, imageObj: UIImageView) {
        let imageURL = URL(string: url)
        do {
            let imageData = try Data(contentsOf: imageURL!)
            imageObj.image = UIImage(data: imageData)
            imageObj.layer.cornerRadius = 50.0
            imageObj.layer.masksToBounds = true
        } catch {
            print("Error processing profile pic: \(error.localizedDescription)")
        }
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
    
    
    // MARK: - Search Controller
    func searchBarIsEmpty() -> Bool {
        return searchController.searchBar.text?.isEmpty ?? true
    }
    
    func filterContentForSearchText(_ searchText: String, scope: String = "All") {
        filteredMembers = MemberCollection.sharedInstance.memberArray.filter({(member : Member) -> Bool in
            let userID = member.getUserID().lowercased().contains(searchText.lowercased())
            let name = member.getFormattedFullName().lowercased().contains(searchText.lowercased())
            let userName = member.getUserName().lowercased().contains(searchText.lowercased())
            let phoneNumber = member.getPhoneNumber().lowercased().contains(searchText.lowercased())
            let email = member.getEmailAddress().lowercased().contains(searchText.lowercased())

            if searchBarIsEmpty() {
                return true
            } else {
                return (userID || name || userName || phoneNumber || email)
            }
        })
        memberListTableView.reloadData()
    }
    
    func isFiltering() -> Bool {
        return searchController.isActive && !searchBarIsEmpty()
    }
    
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
                fatalError("Unexpected sender: \(String(describing: sender))")
            }
            
            guard let indexPath = tableView.indexPath(for: selectedMemberCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            if isFiltering() {
                memberViewController.member = filteredMembers[indexPath.row]
            } else {
                memberViewController.member = MemberCollection.sharedInstance.memberArray[indexPath.row]
            }
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }
    

}

extension MemberListTableViewController: UISearchResultsUpdating {
    // MARK: - UISearchResultsUpdatingDelegate
    func updateSearchResults(for searchController: UISearchController) {
        filterContentForSearchText(searchController.searchBar.text!, scope: "All")
    }
}
