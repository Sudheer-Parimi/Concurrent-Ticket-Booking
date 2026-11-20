package com.sudheer.ticket_booking.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.sudheer.ticket_booking.constant.BookingStatus;
import com.sudheer.ticket_booking.dto.PaymentDTO.PaymentRequest;
import com.sudheer.ticket_booking.dto.PaymentDTO.PaymentResponse;
import com.sudheer.ticket_booking.entity.Booking;
import com.sudheer.ticket_booking.repository.BookingRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("api/payments/")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;
    
    @Autowired 
    private BookingRepository bookingRepository;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequest request) {
        
        try{
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            //razorpay deals with paisa
            orderRequest.put("amount", (int)(request.getAmount()*100));
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_"+request.getBookingId());

            Order order = razorpay.orders.create(orderRequest);

            return ResponseEntity.ok(new PaymentResponse(
                order.get("id"),
                keyId,
                request.getAmount(),
                request.getBookingId()
            ));
        }
        catch(RazorpayException e){
            return ResponseEntity.status(500).body("Error creating payment order: "+ e.getMessage());
        }
    }

    @PostMapping("verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> data) {

        try{
            String orderId = data.get("razorpay_order_id");
            String paymentId = data.get("razorpay_payment_id");
            String signature = data.get("razorpay_signature");
            Long bookingId = Long.parseLong(data.get("bookingId"));
            
            String payload = orderId + "|" + paymentId;

            boolean isValid = Utils.verifySignature(payload, signature, keySecret);

            if(isValid){
                Booking booking = bookingRepository.findById(bookingId).orElseThrow();
                booking.setStatus(BookingStatus.CONFIRMED);
                booking.setTransactionId(paymentId);
                bookingRepository.save(booking);

                return ResponseEntity.ok(Map.of("status","SUCCESS","message","Payment Verified!"));
                
            }
            else{
                return ResponseEntity.badRequest().body("Invalid payment signature");
            }
        }
        catch(Exception e){
            return ResponseEntity.status(500).body("Payment verification failed: "+e.getMessage());
        }
            
    }
    

}
