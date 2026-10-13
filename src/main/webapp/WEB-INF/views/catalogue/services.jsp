<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Services Catalogue" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center max-w-2xl mx-auto mb-16">
            <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Refined Wellness</span>
            <h1 class="text-4xl font-serif font-bold text-salon-900 mt-2">Treatment Catalogue</h1>
            <p class="text-sm text-salon-600 mt-3 font-light">Explore our curated selection of salon and spa therapies performed by master estheticians and stylists.</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <c:forEach items="${services}" var="s">
                <c:set var="sImg" value="https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=600&q=80"/>
                <c:if test="${s.category == 'Hair' and s.name.contains('Color')}">
                    <c:set var="sImg" value="https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${s.category == 'Hair' and !s.name.contains('Color')}">
                    <c:set var="sImg" value="https://images.unsplash.com/photo-1562322140-8baeececf3df?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${s.category == 'Nails' and s.name.contains('Pedicure')}">
                    <c:set var="sImg" value="https://images.unsplash.com/photo-1519014816548-bf5fe059798b?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${s.category == 'Nails' and !s.name.contains('Pedicure')}">
                    <c:set var="sImg" value="https://images.unsplash.com/photo-1632345031435-8727f6897d53?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${s.category == 'Skin'}">
                    <c:set var="sImg" value="https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${s.category == 'Body'}">
                    <c:set var="sImg" value="https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${s.category == 'Beauty'}">
                    <c:set var="sImg" value="https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=600&q=80"/>
                </c:if>

                <div class="bg-salon-50/50 rounded-3xl overflow-hidden border border-salon-100 hover:shadow-xl hover:bg-white transition-all duration-300 flex flex-col justify-between group">
                    <div>
                        <!-- Service Photo -->
                        <div class="relative h-48 overflow-hidden">
                            <img src="${sImg}" alt="${s.name}" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500">
                            <span class="absolute top-3 left-3 px-3 py-1 rounded-full text-xs font-semibold uppercase tracking-wider bg-white/90 backdrop-blur-md text-salon-800 shadow-sm">
                                <c:out value="${s.category}"/>
                            </span>
                            <span class="absolute top-3 right-3 px-2.5 py-1 rounded-full text-xs font-medium bg-black/50 backdrop-blur-md text-white">
                                <i class="fa-regular fa-clock mr-1"></i> ${s.durationFormatted}
                            </span>
                        </div>
                        <div class="p-6">
                            <h3 class="font-serif text-xl font-bold text-salon-900 mb-2"><c:out value="${s.name}"/></h3>
                            <p class="text-xs sm:text-sm text-salon-600 leading-relaxed font-light"><c:out value="${s.description}"/></p>
                        </div>
                    </div>

                    <div class="p-6 pt-0 mt-auto">
                        <div class="pt-4 border-t border-salon-100 flex items-center justify-between">
                            <div>
                                <span class="text-xs text-salon-400 block font-light">Starting at</span>
                                <span class="text-lg font-bold text-salon-900">Rs. ${s.price}</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/customer/appointments/book?serviceId=${s.id}"
                               class="text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-5 py-2.5 rounded-full shadow-sm transition-all">
                                Book Slot
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
