//
//  MainViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 10/14/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit
import FBSDKCoreKit
import FacebookLogin
import Firebase
import FirebaseGoogleAuthUI
import GoogleSignIn


class MainViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, MemberCollectionProtocol, EventCollectionProtocol {
    
    //MemberCollectionProtocol
    //This has a MemberCollection delegate reload the table when the data is finished being loaded
    func memberDataDownloaded() {
        self.upcomingEventsTableView.reloadData()
    }
    
    //EventCollectionProtocol
    //This has a EventCollection delegate reload the table when the data is finished being loaded
    func eventDataDownloaded() {
        if self.refresher.isRefreshing
        {
            self.refresher.endRefreshing()
        }
        self.upcomingEventsTableView.reloadData()
        self.setUpcomingEvents()
        print("UpcomingEvents count: \(upcomingEventsList.count)")
    }
    
    //MARK: - Properties
    @IBOutlet weak var upcomingEventsTableView: UITableView!
    
    var upcomingEventsList: [Event] = []
    var refresher: UIRefreshControl!
    let searchController = UISearchController(searchResultsController: nil)
    let months: [String] = ["J\nA\nN",
                            "F\nE\nB",
                            "M\nA\nR",
                            "A\nP\nR",
                            "M\nA\nY",
                            "J\nU\nN",
                            "J\nU\nL",
                            "A\nU\nG",
                            "S\nE\nP",
                            "O\nC\nT",
                            "N\nO\nV",
                            "D\nE\nC"]
    var filteredEvents = [Event]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        MemberCollection.sharedInstance.delegate = self
        EventCollection.sharedInstance.delegate = self
        EventCollection.sharedInstance.updateFromRemote()
        
        upcomingEventsTableView.delegate = self
        upcomingEventsTableView.dataSource = self
        
        
        //Pull to Refresh
        self.refresher = UIRefreshControl()
        self.refresher?.attributedTitle = NSAttributedString(string: "Pull to refresh")
        self.refresher?.addTarget(self, action: #selector(self.refreshEventList(_:)), for: UIControlEvents.valueChanged)
        self.upcomingEventsTableView?.addSubview(refresher!)
        
        //Search Controller
        searchController.searchResultsUpdater = self
        searchController.obscuresBackgroundDuringPresentation = false
        searchController.searchBar.placeholder = "Search Events"
        navigationItem.searchController = searchController
        definesPresentationContext = true
        
        //Scope Bar
        searchController.searchBar.scopeButtonTitles = ["All", "BBQ", "Emergency", "Medical", "Party", "Unknown"]
        searchController.searchBar.delegate = self
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc private func refreshEventList(_ sender: Any) {
        EventCollection.sharedInstance.updateFromRemote()
    }
    
    func setUpcomingEvents() {
        upcomingEventsList.removeAll()
        let temp = EventCollection.sharedInstance.eventArray
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let calendar = Calendar.current
        let today = Date()
        let futureDate = calendar.date(byAdding: .month, value: 1, to: Date())
        for event in temp {
            let eventDate = dateFormatter.date(from: event.getEventDate())
            if (eventDate! >= today && eventDate! <= futureDate!) {
                upcomingEventsList.append(event)
                print("Event Title: \(event.getTitle())")
            }
        }
    }
    
    // MARK: - Table view data source
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if isFiltering() {
            return filteredEvents.count
        }
        
        return upcomingEventsList.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 50
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "UpcomingEventsTableViewCell"
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? UpcomingEventsTableViewCell  else {
            fatalError("The dequeued cell is not an instance of \(cellIdentifier).")
        }
        
        let event : Event
        if isFiltering() {
            event = filteredEvents[indexPath.row]
        } else {
            event = upcomingEventsList[indexPath.row]
        }
        
        let eventType = event.getEventType().lowercased()
        let eventDate = event.getEventDate()
        let dayIndex = eventDate.index(eventDate.endIndex, offsetBy: -2)
        let dayString = String(eventDate[dayIndex...])
        let monthStartIndex = eventDate.index(eventDate.endIndex, offsetBy: -5)
        let monthEndIndex = eventDate.index(eventDate.endIndex, offsetBy: -3)
        let monthString = String(eventDate[monthStartIndex..<monthEndIndex])
        var progress = Float(event.getCurrentFunds() / event.getFundGoal())
        if (progress < 0.5) {
            cell.progressBar.progressTintColor = UIColor.red
        } else if (progress > 0.5 && progress < 1.0) {
            cell.progressBar.progressTintColor = UIColor.yellow
        } else if (progress >= 1.0) {
            progress = 1.0
            cell.progressBar.progressTintColor = UIColor.green
        }
        cell.nameLabel.text = event.getTitle()
        cell.dayLabel.text = String(Int(dayString)!)
        cell.monthLabel.text = months[Int(monthString)! - 1]
        cell.progressBar.setProgress(progress, animated: true)
        
        switch eventType {
        case "bbq":
            cell.eventIconImageView.image = UIImage(named: "bbq")
        case "emergency":
            cell.eventIconImageView.image = UIImage(named: "emergency")
        case "medical":
            cell.eventIconImageView.image = UIImage(named: "medical")
        case "party":
            cell.eventIconImageView.image = UIImage(named: "party")
        case "unknown":
            cell.eventIconImageView.image = UIImage(named: "unknown")
        default:
            cell.eventIconImageView.image = UIImage(named: "unknown")
        }
        
        let member = MemberCollection.sharedInstance.getMember(userID: event.getAssociatedMember())
        cell.memberLabel.text = member?.getFormattedFullName()
        
        
        return cell
    }
    
    // MARK: - Search Controller
    func searchBarIsEmpty() -> Bool {
        return searchController.searchBar.text?.isEmpty ?? true
    }
    
    func filterContentForSearchText(_ searchText: String, scope: String = "All") {
        filteredEvents = upcomingEventsList.filter({(event : Event) -> Bool in
            let doesEventTypeMatch = (scope == "All") || (event.getEventType() == scope)
            let title = event.getTitle().lowercased().contains(searchText.lowercased())
            let member = event.getAssociatedMember().lowercased().contains(searchText.lowercased())
            let description = event.getEventDescription().lowercased().contains(searchText.lowercased())
            let type = event.getEventType().lowercased().contains(searchText.lowercased())
            let date = event.getEventDate().lowercased().contains(searchText.lowercased())
            
            if searchBarIsEmpty() {
                return doesEventTypeMatch
            } else {
                return doesEventTypeMatch && (title || member || description || type || date)
            }
        })
        upcomingEventsTableView.reloadData()
    }
    
    func isFiltering() -> Bool {
        let searchBarScopeIsFiltering = searchController.searchBar.selectedScopeButtonIndex != 0
        return searchController.isActive && (!searchBarIsEmpty() || searchBarScopeIsFiltering)
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowEvent":
            guard let eventViewController = segue.destination as? ViewEventViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            guard let selectedEventCell = sender as? UpcomingEventsTableViewCell else {
                fatalError("Unexpected sender: \(String(describing: sender))")
            }
            
            guard let indexPath = upcomingEventsTableView.indexPath(for: selectedEventCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            eventViewController.event = upcomingEventsList[indexPath.row]
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }

}

extension MainViewController: UISearchResultsUpdating {
    // MARK: - UISearchResultsUpdatingDelegate
    func updateSearchResults(for searchController: UISearchController) {
        let searchBar = searchController.searchBar
        let scope = searchBar.scopeButtonTitles![searchBar.selectedScopeButtonIndex]
        filterContentForSearchText(searchController.searchBar.text!, scope: scope)
    }
}

extension MainViewController: UISearchBarDelegate {
    // MARK: - UISearchBarDelegate
    func searchBar(_ searchBar: UISearchBar, selectedScopeButtonIndexDidChange selectedScope: Int) {
        filterContentForSearchText(searchBar.text!, scope: searchBar.scopeButtonTitles![selectedScope])
    }
}
