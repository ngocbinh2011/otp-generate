
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <link href="/css/verify.css" rel="stylesheet" type="text/css" media="all" />
</head>
<body>
<div class="d-flex justify-content-center align-items-center container">
    <div class="card py-5 px-3">
        <h5 class="m-0">Email Verification</h5>
        <span class="mobile-text">Enter the code we just send on your email
            <b class="text-danger"> ${email}</b>
        </span>
        <form action="/otp-verify" method="post">
            <div class="d-flex flex-row mt-5">
                <input type="number" min="0" max="9" name="number1" class="form-control" autofocus="">
                <input type="number" min="0" max="9" name="number2" class="form-control">
                <input type="number" min="0" max="9" name="number3" class="form-control">
                <input type="number"min="0" max="9" name="number4" class="form-control">
                <input type="hidden" name="email" value="${email}">
            </div>
            <div class="text-center mt-5">
                <span class="d-block mobile-text">Don't receive the code?</span>
                <span class="font-weight-bold text-danger cursor"><input type="submit" value="Submit"></span>
            </div>
        </form>

    </div>
</div>
</body>
</html>