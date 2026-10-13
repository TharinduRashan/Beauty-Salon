<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Service Form" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8">
            <a href="${pageContext.request.contextPath}/admin/services" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Services</a>
            <h1 class="text-3xl font-serif font-bold text-salon-900">${service != null ? 'Edit Service' : 'Add New Service'}</h1>
        </div>

        <form action="${service != null ? pageContext.request.contextPath.concat('/admin/services/').concat(service.id).concat('/edit') : pageContext.request.contextPath.concat('/admin/services/new')}" method="post" class="space-y-6 bg-salon-50/70 p-8 rounded-3xl border border-salon-100">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div>
                <label for="name" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Service Name</label>
                <input type="text" id="name" name="name" value="<c:out value='${service.name}'/>" required
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                       placeholder="e.g. Aromatherapy Facial">
            </div>

            <div>
                <label for="category" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Category</label>
                <input type="text" id="category" name="category" value="<c:out value='${service.category}'/>" required
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                       placeholder="Hair, Skin, Nails, Body, Beauty">
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label for="durationMinutes" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Duration (Minutes)</label>
                    <input type="number" id="durationMinutes" name="durationMinutes" value="${service != null ? service.durationMinutes : 60}" min="15" max="480" step="15" required
                           class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                </div>
                <div>
                    <label for="price" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Price (LKR)</label>
                    <input type="number" id="price" name="price" value="${service != null ? service.price : 2500}" min="0" step="50" required
                           class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                </div>
            </div>

            <div>
                <label for="description" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Description</label>
                <textarea id="description" name="description" rows="4" required
                          class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                          placeholder="Comprehensive description of the service and treatment steps..."><c:out value="${service.description}"/></textarea>
            </div>

            <button type="submit" class="w-full text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3.5 rounded-2xl shadow-sm transition-all">
                Save Service
            </button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
