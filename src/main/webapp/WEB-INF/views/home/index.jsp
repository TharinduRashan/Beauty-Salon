<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<!-- Hero Section -->
<section class="relative overflow-hidden pt-12 pb-24 md:pt-20 md:pb-32 bg-gradient-to-b from-white to-salon-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-12 items-center">
            <div class="space-y-6">
                <span class="inline-flex items-center px-4 py-1.5 rounded-full text-xs font-semibold tracking-wider uppercase bg-salon-100 text-salon-700">
                    <i class="fa-solid fa-sparkles mr-2 text-salon-500"></i> Pure Radiance & Rejuvenation
                </span>
                <h1 class="text-4xl sm:text-5xl lg:text-6xl font-serif font-bold tracking-tight text-salon-900 leading-tight">
                    Where Style Meets Tranquility.
                </h1>
                <p class="text-base sm:text-lg text-salon-700 leading-relaxed font-light">
                    Experience luxurious hair, skin, and spa treatments customized exclusively for your lifestyle. Step into an oasis of relaxation and bespoke beauty care in the heart of the city.
                </p>
                <div class="pt-4 flex flex-col sm:flex-row items-center gap-4">
                    <a href="${pageContext.request.contextPath}/customer/appointments/book" class="w-full sm:w-auto text-center font-medium text-white bg-salon-600 hover:bg-salon-700 px-8 py-4 rounded-full shadow-lg shadow-salon-500/20 hover:shadow-xl hover:shadow-salon-500/30 transition-all duration-300">
                        Book Your Experience
                    </a>
                    <a href="${pageContext.request.contextPath}/services" class="w-full sm:w-auto text-center font-medium text-salon-800 bg-white hover:bg-salon-50 px-8 py-4 rounded-full border border-salon-200 transition-all duration-300">
                        Explore Catalogue
                    </a>
                </div>
            </div>

            <div class="relative">
                <div class="relative mx-auto max-w-md md:max-w-none">
                    <!-- Luxury Salon Hero Card with High-Res Image -->
                    <div class="relative rounded-3xl overflow-hidden shadow-2xl border-4 border-white group">
                        <img src="https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=1000&q=80"
                             alt="Luxury Beauty Salon Interior"
                             class="w-full h-[460px] object-cover object-center group-hover:scale-105 transition-transform duration-700">
                        <div class="absolute inset-0 bg-gradient-to-t from-salon-900/90 via-salon-900/30 to-transparent flex flex-col justify-end p-8 text-white">
                            <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-white/20 backdrop-blur-md text-white mb-2 w-max">
                                <i class="fa-solid fa-gem mr-1.5 text-amber-300"></i> Bespoke Craftsmanship
                            </span>
                            <h3 class="font-serif text-2xl font-bold tracking-tight">Aesthetic Mastery</h3>
                            <p class="text-xs text-salon-100/90 mt-1 leading-relaxed max-w-xs font-light">Elevated styling, mindful therapy, and natural wellness designed to unveil your authentic glow.</p>
                        </div>
                    </div>
                    <!-- Floating Rating Badge -->
                    <div class="absolute -bottom-5 -left-5 bg-white/95 backdrop-blur-md p-4 rounded-2xl shadow-xl border border-salon-100 flex items-center space-x-3 hidden sm:flex">
                        <div class="w-10 h-10 rounded-full bg-amber-50 text-amber-500 flex items-center justify-center font-bold text-sm">
                            ★ 4.9
                        </div>
                        <div>
                            <div class="text-xs font-bold text-salon-900">Premier Salon Choice</div>
                            <div class="text-[10px] text-salon-500">Over 1,200+ Discerning Guests</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Featured Services Section -->
<section class="py-20 bg-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-col md:flex-row justify-between items-end mb-12">
            <div>
                <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Handcrafted Treatments</span>
                <h2 class="text-3xl font-serif font-bold text-salon-900 mt-2">Signature Services</h2>
            </div>
            <a href="${pageContext.request.contextPath}/services" class="mt-4 md:mt-0 text-sm font-semibold text-salon-600 hover:text-salon-800 inline-flex items-center">
                View full menu <i class="fa-solid fa-arrow-right ml-2 text-xs"></i>
            </a>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            <c:forEach items="${featuredServices}" var="svc">
                <c:set var="svcImg" value="https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=600&q=80"/>
                <c:if test="${svc.category == 'Hair' and svc.name.contains('Color')}">
                    <c:set var="svcImg" value="https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${svc.category == 'Hair' and !svc.name.contains('Color')}">
                    <c:set var="svcImg" value="https://images.unsplash.com/photo-1562322140-8baeececf3df?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${svc.category == 'Nails' and svc.name.contains('Pedicure')}">
                    <c:set var="svcImg" value="https://images.unsplash.com/photo-1519014816548-bf5fe059798b?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${svc.category == 'Nails' and !svc.name.contains('Pedicure')}">
                    <c:set var="svcImg" value="https://images.unsplash.com/photo-1632345031435-8727f6897d53?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${svc.category == 'Skin'}">
                    <c:set var="svcImg" value="https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${svc.category == 'Body'}">
                    <c:set var="svcImg" value="https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=600&q=80"/>
                </c:if>
                <c:if test="${svc.category == 'Beauty'}">
                    <c:set var="svcImg" value="https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=600&q=80"/>
                </c:if>

                <div class="bg-salon-50/60 rounded-3xl overflow-hidden border border-salon-100/80 hover:shadow-xl hover:bg-white transition-all duration-300 flex flex-col justify-between group">
                    <div>
                        <!-- Service Image Container -->
                        <div class="relative h-44 overflow-hidden">
                            <img src="${svcImg}" alt="${svc.name}" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500">
                            <span class="absolute top-3 left-3 px-3 py-1 rounded-full text-[11px] font-semibold uppercase tracking-wider bg-white/90 backdrop-blur-md text-salon-800 shadow-sm">
                                <c:out value="${svc.category}"/>
                            </span>
                            <span class="absolute top-3 right-3 px-2.5 py-1 rounded-full text-[11px] font-medium bg-black/50 backdrop-blur-md text-white">
                                <i class="fa-regular fa-clock mr-1"></i> ${svc.durationFormatted}
                            </span>
                        </div>
                        <div class="p-6">
                            <h3 class="font-serif text-lg font-bold text-salon-900 mb-1.5"><c:out value="${svc.name}"/></h3>
                            <p class="text-xs text-salon-600 line-clamp-2 leading-relaxed font-light"><c:out value="${svc.description}"/></p>
                        </div>
                    </div>
                    <div class="p-6 pt-0 mt-auto">
                        <div class="pt-4 border-t border-salon-100 flex items-center justify-between">
                            <div>
                                <span class="text-[10px] uppercase tracking-wider text-salon-400 block font-light">Starting at</span>
                                <span class="text-base font-bold text-salon-900">Rs. ${svc.price}</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/customer/appointments/book?serviceId=${svc.id}" class="w-10 h-10 rounded-full bg-salon-600 hover:bg-salon-700 text-white flex items-center justify-center transition-all shadow-sm">
                                <i class="fa-solid fa-calendar-plus text-xs"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Testimonials Section -->
<c:if test="${not empty approvedReviews}">
    <section class="py-20 bg-salon-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="text-center max-w-xl mx-auto mb-16">
                <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">What Our Guests Say</span>
                <h2 class="text-3xl font-serif font-bold text-salon-900 mt-2">Loved by Discerning Clients</h2>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                <c:forEach items="${approvedReviews}" var="rev">
                    <div class="bg-white rounded-3xl p-8 border border-salon-100 shadow-sm flex flex-col justify-between">
                        <div>
                            <div class="text-amber-400 text-sm mb-4 tracking-widest">
                                ${rev.ratingStars}
                            </div>
                            <h4 class="font-serif font-bold text-salon-900 mb-2"><c:out value="${rev.title}"/></h4>
                            <p class="text-xs sm:text-sm text-salon-600 leading-relaxed italic font-light">"<c:out value="${rev.content}"/>"</p>
                        </div>
                        <div class="mt-6 pt-4 border-t border-salon-50 flex items-center justify-between text-xs text-salon-500">
                            <span class="font-semibold text-salon-800"><c:out value="${rev.customerName != null ? rev.customerName : 'Satisfied Client'}"/></span>
                            <c:if test="${not empty rev.serviceNameSnapshot}">
                                <span><c:out value="${rev.serviceNameSnapshot}"/></span>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>
</c:if>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
