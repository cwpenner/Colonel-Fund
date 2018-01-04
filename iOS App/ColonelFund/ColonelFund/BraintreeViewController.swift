//
//  BraintreeViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/2/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import UIKit
import BraintreeDropIn
import Braintree

class BraintreeViewController: UIViewController, UITextFieldDelegate, PKPaymentAuthorizationViewControllerDelegate {
    //Constructor
    func BraintreeViewController(donationTextField: UITextField, donateButton: UIButton, paymentDescriptionLabel: UILabel, selectPaymentButton: UIButton, paymentIconView: UIImageView) {
        self.donationTextField = donationTextField
        self.donateButton = donateButton
        self.paymentDescriptionLabel = paymentDescriptionLabel
        self.selectPaymentButton = selectPaymentButton
        self.paymentIconView = paymentIconView
    }
    
    override func viewDidLoad() {
        //In view controllers using this class, IBOutlets from accessing view controller must declare respective IBOutlets above corresponding super.viewDidLoad() method to link them before this class processes them (ie. donationTextField.delegate = self will error if donationTextField is not passed an object from accessing view controller first)
        super.viewDidLoad()
        
        donationTextField.delegate = self
        
        self.fetchExistingPaymentMethod(clientToken: token)
        
        self.view.addGestureRecognizer(UITapGestureRecognizer(target: self.view, action: #selector(UIView.endEditing(_:))))
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //MARK: Properties
    var donationAmount: NSDecimalNumber = 0.00
    var memberName: String = "" //TODO: consider changing to Member object, once implemented
    var eventTitle: String = "" //TODO: consider changing to Event object, once implemented
    
    //API Tokenization Key
    let token = "sandbox_3swsvvz5_mhbr9s54673smz3g"
    
    //MARK: Payment info
    var paymentMethod: BTPaymentMethodNonce = BTPaymentMethodNonce()
    var useApplePay = false
    var applePayNonce: String = ""
    
    //MARK: IBOutlets
    var donationTextField: UITextField!
    var donateButton: UIButton!
    var paymentDescriptionLabel: UILabel!
    var selectPaymentButton: UIButton!
    var paymentIconView: UIImageView!
    
    //MARK: IBActions
    func donateButtonPressed() {
        donationAmount = NSDecimalNumber(string: donationTextField.text!)
        print("Donation Amount: \(donationAmount.stringValue)")
        var nonce = paymentMethod.nonce
        
        if (self.useApplePay) {
            tappedApplePay()
            nonce = applePayNonce
        }
        
        self.sendRequestPaymentToServer(nonce: nonce, amount: donationAmount.stringValue)
        //TODO: segue to Thank You screen
    }
    
    func selectPaymentButtonPressed() {
        showDropIn(clientTokenOrTokenizationKey: token)
    }
    
    //MARK: Braintree Functions
    //Displays Drop In view
    func showDropIn(clientTokenOrTokenizationKey: String){
        let request =  BTDropInRequest()
        let dropIn = BTDropInController(authorization: clientTokenOrTokenizationKey, request: request)
        { (controller, result, error) in
            if (error != nil) {
                print("Error showing Braintree DropIn")
            } else if (result?.isCancelled == true) {
                print("Cancelled showing Braintree DropIn")
            } else if let result = result {
                print("Icon: \(result.paymentIcon)")
                print("Desc: \(result.paymentDescription)")
                print("PaymentMethod: \(result.paymentMethod)")
                print("PaymentMethodOption: \(result.paymentMethod?.type)")
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
        self.present(dropIn!, animated: true, completion: nil)
    }
    
    //Retrieves previously used payment methods, if client token is used
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
//            let paymentMethodType: BTUIKPaymentOptionType = BTUIKViewUtil.paymentOptionType(forPaymentInfoType: paymentMethodNonce!.type)
            updatePaymentImage(paymentOptionText: paymentMethodNonce!.type)
            self.paymentDescriptionLabel.text = paymentMethodNonce?.localizedDescription
            self.selectPaymentButton.setTitle("Change Payment Method", for: UIControlState.normal)
            if (self.donationTextField.text != "") {
                self.donateButton.isEnabled = true
            }
        }
    }
    
    func setupApplePay() {
        self.paymentDescriptionLabel.isHidden = false
        self.paymentIconView.isHidden = false
        self.paymentDescriptionLabel.text = "Apple Pay"
        updatePaymentImage(paymentOptionText: "ApplePay")
        self.selectPaymentButton.setTitle("Change Payment Method", for: UIControlState.normal)
        self.useApplePay = true
//        self.donateButton = PKPaymentButton(paymentButtonType: PKPaymentButtonType.donate, paymentButtonStyle: PKPaymentButtonStyle.black) //cannot dynamically change button after run, would need to make new button programmatically and programmatically assign constraints
        if (self.donationTextField.text != "") {
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
    
    func updatePaymentImage(paymentOptionText: String) {
        var iconArt: BTUIKVectorArtView
        if (paymentOptionText == "ApplePay") {
            iconArt = BTUIKViewUtil.vectorArtView(for: BTUIKPaymentOptionType.applePay)
        } else {
            iconArt = BTUIKViewUtil.vectorArtView(forPaymentInfoType: paymentOptionText)
        }
        let paymentIcon: UIImage = iconArt.image(of: CGSize(width: 45, height: 29))
        paymentIconView.contentMode = UIViewContentMode.center
        paymentIconView.image = paymentIcon
    }
    
    //MARK: Text Field Input Control
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if string.isEmpty {
            self.donateButton.isEnabled = false
            return true
        } else if (Double(string) != 0) {
            if (self.paymentMethod.nonce != "" || useApplePay == true) {
                self.donateButton.isEnabled = true
            }
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
    
    //MARK: Apple Pay
    func paymentRequest() -> PKPaymentRequest {
        let paymentRequest = PKPaymentRequest()
        let donationLabel: String
        if (eventTitle != "") {
            donationLabel = eventTitle
        } else {
            donationLabel = memberName
        }
        paymentRequest.merchantIdentifier = "merchant.sandbox.com.ColonelFund.ColonelFund"
        paymentRequest.supportedNetworks = [PKPaymentNetwork.amex, PKPaymentNetwork.visa, PKPaymentNetwork.masterCard]
        paymentRequest.merchantCapabilities = PKMerchantCapability.capability3DS
        paymentRequest.countryCode = "US"
        paymentRequest.currencyCode = "USD"
        paymentRequest.paymentSummaryItems = [
            PKPaymentSummaryItem(label: "Donation to \(donationLabel)", amount: self.donationAmount)
            // Add additional payment summary items...
        ]
        return paymentRequest
    }
    
    func tappedApplePay() {
        let paymentRequest = self.paymentRequest()
        // Example: Promote PKPaymentAuthorizationViewController to optional so that we can verify
        // that our paymentRequest is valid. Otherwise, an invalid paymentRequest would crash our app.
        if let vc = PKPaymentAuthorizationViewController(paymentRequest: paymentRequest)
            as PKPaymentAuthorizationViewController? {
            vc.delegate = self
            present(vc, animated: true, completion: nil)
        } else {
            print("Error: Payment request is invalid.")
        }
    }
    
    func paymentAuthorizationViewController(_ controller: PKPaymentAuthorizationViewController,
                                            didAuthorizePayment payment: PKPayment, completion: @escaping (PKPaymentAuthorizationStatus) -> Void) {
        
        // Example: Tokenize the Apple Pay payment
        let applePayClient = BTApplePayClient(apiClient: BTAPIClient(authorization: token)!)
        applePayClient.tokenizeApplePay(payment) {
            (tokenizedApplePayPayment, error) in
            guard let tokenizedApplePayPayment = tokenizedApplePayPayment else {
                // Tokenization failed. Check `error` for the cause of the failure.
                
                // Indicate failure via completion callback.
                completion(PKPaymentAuthorizationStatus.failure)
                
                return
            }
            
            // Received a tokenized Apple Pay payment from Braintree.
            // If applicable, address information is accessible in `payment`.
            
            // Send the nonce to your server for processing.
            self.applePayNonce = tokenizedApplePayPayment.nonce
            print("Apple Pay Braintree nonce = \(self.applePayNonce)")
            
            // Then indicate success or failure via the completion callback, e.g.
            completion(PKPaymentAuthorizationStatus.success)
        }
    }
    
    // Be sure to implement paymentAuthorizationViewControllerDidFinish.
    // You are responsible for dismissing the view controller in this method.
    @available(iOS 8.0, *)
    func paymentAuthorizationViewControllerDidFinish(_ controller: PKPaymentAuthorizationViewController) {
        dismiss(animated: true, completion: nil)
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
}

extension String {
    func isValidDouble(maxDecimalPlaces: Int) -> Bool {
        // Use NumberFormatter to check if we can turn the string into valid number and get the locale specific decimal separator
        let formatter = NumberFormatter()
        formatter.allowsFloats = true
        let decimalSeparator = formatter.decimalSeparator ?? "."  // Gets locale specific decimal separator. Use "." if not specified
        
        // Check if string is valid number
        if formatter.number(from: self) != nil {
            let split = self.components(separatedBy: decimalSeparator)
            let digits = split.count == 2 ? split.last ?? "" : ""
            
            // Return true if number of digits after decimal is valid
            return digits.count <= maxDecimalPlaces
        }
        
        return false // couldn't turn string into a valid number
    }
}
