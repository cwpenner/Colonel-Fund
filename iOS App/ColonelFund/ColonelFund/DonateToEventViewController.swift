//
//  DonateToEventViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit
import BraintreeDropIn
import Braintree

class DonateToEventViewController: UIViewController, UITextFieldDelegate {

    //MARK: Properties
    @IBOutlet weak var eventTitleLabel: UILabel!
    @IBOutlet weak var eventDateLabel: UILabel!
    @IBOutlet weak var eventMemberLabel: UILabel!
    @IBOutlet weak var eventFundGoalLabel: UILabel!
    @IBOutlet weak var eventCurrentFundsLabel: UILabel!
    @IBOutlet weak var eventDonationTextField: UITextField!
    @IBOutlet weak var donateButton: UIButton!
    @IBOutlet weak var paymentDescriptionLabel: UILabel!
    @IBOutlet weak var selectPaymentButton: UIButton!
    @IBOutlet weak var paymentIconView: UIView!
    
    var tempTitleText: String = ""
    var tempDateText: String = "2018-07-04"
    var tempMemberText: String = "John Smith"
    var tempFundGoalText: String = "$1,000.00"
    var tempCurrentFundsText: String = "$75.63"
    
    //MARK: Payment info
    //API Tokenization Key
    let token = "sandbox_3swsvvz5_mhbr9s54673smz3g"
    var paymentMethod: BTPaymentMethodNonce = BTPaymentMethodNonce()
    var paymentIcon: BTUIKPaymentOptionCardView = BTUIKPaymentOptionCardView()
    var useApplePay = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        eventDonationTextField.delegate = self
        eventTitleLabel.text = tempTitleText
        eventDateLabel.text = tempDateText
        eventMemberLabel.text = tempMemberText
        eventFundGoalLabel.text = tempFundGoalText
        eventCurrentFundsLabel.text = tempCurrentFundsText
        
        self.paymentIcon.translatesAutoresizingMaskIntoConstraints = false
        self.paymentIconView.addSubview(self.paymentIcon)
        
        self.fetchExistingPaymentMethod(clientToken: token)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if string.isEmpty {
            self.donateButton.isEnabled = false
            return true
        } else if (Double(string) != 0) {
            self.donateButton.isEnabled = true
        }
        
        // Build the full current string: TextField right now only contains the
        // previous valid value. Use provided info to build up the new version.
        // Can't just concat the two strings because the user might've moved the
        // cursor and delete something in the middle.
        let currentText = textField.text ?? ""
        let replacementText = (currentText as NSString).replacingCharacters(in: range, with: string)
        
        // Use custom string extension to check if the string is valid double with specified amount of decimal places
        return replacementText.isValidDouble(maxDecimalPlaces: 2)
    }
    
    func textFieldShouldClear(_ textField: UITextField) -> Bool {
        self.donateButton.isEnabled = false
        return true
    }
    
    @IBAction func donateButtonPressed(_ sender: Any) {
        print("Donation Amount: \(self.eventDonationTextField.text!)")
        self.sendRequestPaymentToServer(nonce: self.paymentMethod.nonce, amount: eventDonationTextField.text!)
    }

    @IBAction func selectPaymentButtonPressed(_ sender: Any) {
        showDropIn(clientTokenOrTokenizationKey: token, targetViewController: self)
    }
    
    func showDropIn(clientTokenOrTokenizationKey: String, targetViewController: UIViewController){
        let request =  BTDropInRequest()
        let dropIn = BTDropInController(authorization: clientTokenOrTokenizationKey, request: request)
        { (controller, result, error) in
            if (error != nil) {
                print("Error showing Braintree DropIn")
            } else if (result?.isCancelled == true) {
                print("Cancelled showing Braintree DropIn")
            } else if let result = result {
                if (result.paymentOptionType == BTUIKPaymentOptionType.applePay) {
                    self.setupApplePay()
                } else {
                    self.useApplePay = false;
                    self.paymentMethod = result.paymentMethod!;
                    self.updatePaymentMethod(paymentMethodNonce: self.paymentMethod)
                }
                
            }
            controller.dismiss(animated: true, completion: nil)
        }
        targetViewController.present(dropIn!, animated: true, completion: nil)
    }
    
    func fetchExistingPaymentMethod(clientToken: String) {
        BTDropInResult.fetch(forAuthorization: clientToken, handler: { (result, error) in
            if (error != nil) {
                print("Error fetching previous payment method")
            } else if let result = result {
                if (result.paymentOptionType == BTUIKPaymentOptionType.applePay) {
                    self.setupApplePay()
                } else {
                    self.useApplePay = false;
                    self.paymentMethod = result.paymentMethod!;
                    self.updatePaymentMethod(paymentMethodNonce: self.paymentMethod)
                }
            }
        })
    }
    
    func updatePaymentMethod(paymentMethodNonce: BTPaymentMethodNonce?) {
        self.paymentIconView.isHidden = paymentMethodNonce!.nonce == ""
        self.paymentDescriptionLabel.isHidden = paymentMethodNonce!.nonce == ""
        if (paymentMethodNonce!.nonce != "") {
            let paymentMethodType: BTUIKPaymentOptionType = BTUIKViewUtil.paymentOptionType(forPaymentInfoType: paymentMethodNonce!.type)
            self.paymentIcon.paymentOptionType = paymentMethodType //**Update payment icon
            self.paymentDescriptionLabel.text = paymentMethodNonce?.localizedDescription
            self.selectPaymentButton.setTitle("Change Payment Method", for: UIControlState.normal)
            if (self.eventDonationTextField.text != "") {
                self.donateButton.isEnabled = true
            }
        }
    }
    
    func setupApplePay() {
        self.paymentDescriptionLabel.isHidden = false
        self.paymentIcon.isHidden = false
        self.paymentIcon.paymentOptionType = BTUIKPaymentOptionType.applePay
        self.paymentDescriptionLabel.text = "Apple Pay"
        self.useApplePay = true
        if (self.eventDonationTextField.text != "") {
            self.donateButton.isEnabled = true
        }
    }
    
    func sendRequestPaymentToServer(nonce: String, amount: String) {
        //**Change with own server URL
        let paymentURL = URL(string: "https://localhost/braintree/pay.php")!
        var request = URLRequest(url: paymentURL)
        request.httpBody = "payment_method_nonce=\(nonce)&amount=\(amount)".data(using: String.Encoding.utf8)
        request.httpMethod = "POST"
        
        URLSession.shared.dataTask(with: request) { (data, response, error) -> Void in
            guard let data = data else {
                print(error!.localizedDescription)
                return
            }
            
            guard let result = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any], let success = result?["success"] as? Bool, success == true else {
                print("Transaction failed. Please try again.")
                return
            }
            
            print("Successfully charged.")
            }.resume()
    }
    
    // MARK: - BTViewControllerPresentingDelegate
    func paymentDriver(_ driver: Any, requestsPresentationOf viewController: UIViewController) {
        present(viewController, animated: true, completion: nil)
    }
    
    func paymentDriver(_ driver: Any, requestsDismissalOf viewController: UIViewController) {
        viewController.dismiss(animated: true, completion: nil)
    }
    
    // MARK: - BTAppSwitchDelegate - OPTIONAL
    // Optional - display and hide loading indicator UI
//    func appSwitcherWillPerformAppSwitch(_ appSwitcher: Any) {
//        showLoadingUI()
//
//        NotificationCenter.default.addObserver(self, selector: #selector(hideLoadingUI), name: NSNotification.Name.UIApplicationDidBecomeActive, object: nil)
//    }
//
//    func appSwitcherWillProcessPaymentInfo(_ appSwitcher: Any) {
//        hideLoadingUI()
//    }
//
//    func appSwitcher(_ appSwitcher: Any, didPerformSwitchTo target: BTAppSwitchTarget) {
//
//    }
//
//    // MARK: - Private methods
//
//    func showLoadingUI() {
//        // ...
//    }
//
//    @objc func hideLoadingUI() {
//        NotificationCenter
//            .default
//            .removeObserver(self, name: NSNotification.Name.UIApplicationDidBecomeActive, object: nil)
//        // ...
//    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
