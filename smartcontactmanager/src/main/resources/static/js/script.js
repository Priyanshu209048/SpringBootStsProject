console.log("This is script file");

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		//true
		//Close the side bar

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		//false
		//Open the side bar
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};

const search = () => {
	// console.log("Searching");
	let query = $("#search-input").val();

	if (query == "") {
		$(".search-result").hide();
	} else {
		console.log(query);

		//Sending request to server
		let url = `http://localhost:8080/search/${query}`;
		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
				//data
				//console.log(data);

				let text = `<div class='list-group'>`;

				data.forEach((contact) => {
					text += `<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'> ${contact.name} </a>`;
				});

				text += `</div>`;

				$(".search-result").html(text);
				$(".search-result").show();
			});

		$(".search-result").show();
	}
};

//First request to server to create order
const paymentStart = () => {
	console.log("Payment Sarted...");

	let amount = $("#payment_field").val();
	console.log(amount);

	if (amount == "" || amount == null) {
		//alert("Please enter the correct Amount!");
		swal({
			title: "Failed!",
			text: "Please enter the correct Amount!",
			icon: "error"
		});
		return;
	}

	//We will use ajax to send request to server to create order - jQuery
	$.ajax({
		url: "/user/create_order",
		data: JSON.stringify({ amount: amount, info: "order_request" }),
		contentType: "application/json",
		type: "POST",
		dataType: "json",
		success: function(response) {
			//invoked when success
			console.log(response);
			if (response.status == "created") {
				console.log("Entering into payment gateway")
				// open payment form
				let options = {
					key: "rzp_test_unGf0fgN6itTxn",
					amount: response.amount,
					currency: "INR",
					name: "Smart Contact Manager",
					description: "Donation",
					image:
						"https://images.unsplash.com/photo-1534670007418-fbb7f6cf32c3?q=80&w=1888&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
					order_id: response.id,
					handler: function(response) {
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						console.log("Payment Successfull...");

						updatePaymentOnServer(
							response.razorpay_payment_id,
							response.razorpay_order_id,
							"paid"
						);

						//alert("Congrates! Your Payment is Successful.");
						
					},
					prefill: {
						name: "",
						email: "",
						contact: "",
					},
					notes: {
						address: "Priyanshu Baral Office",
					},
					theme: {
						color: "#3399cc",
					},
				};

				//Initiating a payment
				let rzp = new Razorpay(options);

				rzp.on("payment.failed", function(response) {
					console.log(response.error.code);
					console.log(response.error.description);
					console.log(response.error.source);
					console.log(response.error.step);
					console.log(response.error.reason);
					console.log(response.error.metadata.order_id);
					console.log(response.error.metadata.payment_id);
					//alert("Your Payment is Unsuccessfull!! ")
					swal({
						title: "Failed!",
						text: "Your Payment is Failed!",
						icon: "error"
					});
				});

				rzp.open();
			}
			console.log("Exiting the gateway")
		},
		error: function(error) {
			//invoked when error
			console.log(error);
			alert("Error! Please try again.");
		},
	});
};

//
function updatePaymentOnServer(payment_id,order_id,statuses){
	$.ajax({
		url: "/user/update_order",
		data: JSON.stringify({ payment_id: payment_id, order_id: order_id, status: statuses }),
		contentType: "application/json",
		type: "POST",
		dataType: "json",
		success:function(response){
			swal({
				title: "Congrates!",
				text: "Your Payment is Successful!",
				icon: "success"
			});
		},
		error:function(error){
			swal({
				title: "Failed!",
				text: "Your Payment is successful, but we did not capture it, we will contact you as soon as possible!",
				icon: "error"
			});
		},
	});
}
