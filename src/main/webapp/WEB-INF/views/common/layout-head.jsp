<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle != null ? pageTitle : "Glamour Beauty Salon"} | Luxury Salon & Spa</title>
    <!-- Favicon -->
    <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Cdefs%3E%3ClinearGradient id='g' x1='0%25' y1='0%25' x2='100%25' y2='100%25'%3E%3Cstop offset='0%25' stop-color='%238B5E83'/%3E%3Cstop offset='100%25' stop-color='%234A2744'/%3E%3C/linearGradient%3E%3C/defs%3E%3Crect width='100' height='100' rx='24' fill='url(%23g)'/%3E%3Cpath d='M50 16 C51 32 68 49 84 50 C68 51 51 68 50 84 C49 68 32 51 16 50 C32 49 49 32 50 16 Z' fill='%23FAF7F5'/%3E%3Ccircle cx='50' cy='50' r='7' fill='%23D4A5B5'/%3E%3Ccircle cx='72' cy='28' r='3.5' fill='%23FAF7F5'/%3E%3Ccircle cx='28' cy='72' r='2.5' fill='%23FAF7F5'/%3E%3C/svg%3E">
    <link rel="alternate icon" type="image/svg+xml" href="${pageContext.request.contextPath}/images/favicon.svg">
    <link rel="apple-touch-icon" href="${pageContext.request.contextPath}/images/favicon.svg">
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        salon: {
                            50: '#FAF7F5',
                            100: '#F5EBE6',
                            200: '#EBDCD5',
                            300: '#D4A5B5',
                            400: '#B87D96',
                            500: '#8B5E83',
                            600: '#754A6E',
                            700: '#603859',
                            800: '#4A2744',
                            900: '#2C1810',
                        }
                    }
                }
            }
        }
    </script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,400;0,600;0,700;1,400&family=Plus+Jakarta+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: #FAF7F5;
            color: #2C1810;
        }
        .font-serif {
            font-family: 'Playfair Display', serif;
        }
    </style>
</head>
<body class="min-h-screen flex flex-col bg-salon-50 text-salon-900">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <jsp:include page="/WEB-INF/views/common/messages.jsp" />
