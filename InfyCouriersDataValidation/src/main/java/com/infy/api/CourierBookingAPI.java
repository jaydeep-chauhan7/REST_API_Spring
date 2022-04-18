package com.infy.api;


import javax.validation.Valid;
import javax.validation.constraints.Max;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.BookingDTO;
import com.infy.exception.InfyCourierException;
import com.infy.service.BookingService;

@RestController
@RequestMapping(value="/infycourier")
@Validated
public class CourierBookingAPI {
	@Autowired
	private BookingService bookingService;
	@Autowired
	private Environment environment;
	/*This is a REST controller method to book a courier.
It accepts POST request with URI /courier.
It receives booking details in the form of JSON. The JSON data should be populated in BookingDTO parameter of this method after successful validation.
Using Bean Validation API validate the booking details according to the description given below (Note: The validation message should not be hard-coded. It should be read from the ValidationMessages.properties file):.
weight
If weight is not provided then set Weight is mandatory as the validation message.
If provided it should be more than or equal to  30, else set Please enter weight above 30 grams as the validation message.
priority
If priority is not provided then set Priority is mandatory as the validation message.
If provided priority should be LOW, MEDIUM or HIGH, else set Priority can either be LOW, MEDIUM or HIGH as the validation message.
status
If status is provided then set Please do not set status as validation message.
source
If source is not provided then set Source is mandatory as the validation message.
If it is provided, then it should be a combination of alphabets and space. Also, it should start with an alphabet. If invalid, set Enter source in valid format as the validation message
destination
If destination is not provided then set Destination is mandatory as the validation message.
If it is provided, then it should be a combination of alphabets and space. Also, it should start with an alphabet. If invalid, set Enter destination in valid format as the validation message
Invoke the bookCourier() method of BookingServiceImpl, which returns a booking id.
Retrieve the success message associated with property API.BOOKING_SUCCESS from application.properties files using the environment and append it to booking id in the format : <success message> : booking id
Return an object of ResponseEntity created using the above message and HTTP status code as CREATED.*/
	@PostMapping(value="/courier")
	public ResponseEntity<String> bookCourier(@Valid @RequestBody BookingDTO bookingDTO) throws InfyCourierException {
		int bookingId=bookingService.bookCourier(bookingDTO);
		String message=environment.getProperty("API.BOOKING_SUCCESS"+bookingId);
		return new ResponseEntity<>(message,HttpStatus.CREATED);
	}
	/*This is a REST controller method to get details of a particular courier details. 
		It accepts GET request with URI /courier/{bookingId}
		The path variable bookingId must be validated as per the following conditions using Bean Validation API:
		bookingId should not have length more than 5. If invalid set Booking Id should be of 5 digits as validation message.
		Invoke the getCourier() method of BookingServiceImpl, which returns a bookingDTO object.
		Return an object of ResponseEntity created using the above bookingDTO object and HTTP status code as OK.*/
	@GetMapping(value="/courier/{bookingId}")
	public ResponseEntity<BookingDTO> getCourierDetails(@PathVariable @Max(value=5,message="{booking.invalid.bookingId}") Integer bookingId) throws InfyCourierException {
		BookingDTO book=bookingService.getCourierDetail(bookingId);
		return new ResponseEntity<>(book,HttpStatus.OK);
	}

}
