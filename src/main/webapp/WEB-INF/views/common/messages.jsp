<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-4 w-full">
    <c:if test="${not empty successMessage}">
        <div class="p-4 mb-4 text-sm text-green-800 rounded-2xl bg-green-50 border border-green-200 flex items-center justify-between shadow-sm animate-fade-in" role="alert">
            <div class="flex items-center">
                <i class="fa-solid fa-circle-check text-green-600 text-lg mr-3"></i>
                <span class="font-medium"><c:out value="${successMessage}"/></span>
            </div>
            <button type="button" class="text-green-600 hover:text-green-800 focus:outline-none" onclick="this.parentElement.remove();">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="p-4 mb-4 text-sm text-red-800 rounded-2xl bg-red-50 border border-red-200 flex items-center justify-between shadow-sm animate-fade-in" role="alert">
            <div class="flex items-center">
                <i class="fa-solid fa-circle-exclamation text-red-600 text-lg mr-3"></i>
                <span class="font-medium"><c:out value="${errorMessage}"/></span>
            </div>
            <button type="button" class="text-red-600 hover:text-red-800 focus:outline-none" onclick="this.parentElement.remove();">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>
    </c:if>
</div>
