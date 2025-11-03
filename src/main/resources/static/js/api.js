const API_BASE_URL = 'http://localhost:8049/api'; // Port 8049

async function apiCall(endpoint, method = 'GET', data = null) {
    console.log('🔄 API Call:', `${API_BASE_URL}${endpoint}`, method);

    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include' // Important for CORS with credentials
    };

    if (data) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

        // Check if response is OK
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP ${response.status}: ${errorText}`);
        }

        const result = await response.json();
        console.log('✅ API Success:', result);
        return result;

    } catch (error) {
        console.error('❌ API Error:', error);
        throw error;
    }
}

// User APIs
const userAPI = {
    login: (email) => apiCall('/users/email/' + email),
    createUser: (userData) => apiCall('/users', 'POST', userData),
    getUserById: (id) => apiCall(`/users/${id}`),
    getAllUsers: () => apiCall('/users')
};

// Course APIs
const courseAPI = {
    getAllCourses: () => apiCall('/courses'),
    getCourseById: (id) => apiCall(`/courses/${id}`),
    createCourse: (courseData) => apiCall('/courses', 'POST', courseData),
    searchCourses: (query) => apiCall(`/courses/search?query=${encodeURIComponent(query)}`)
};

// Enrollment APIs
const enrollmentAPI = {
    createEnrollment: (enrollmentData) => apiCall('/enrollments', 'POST', enrollmentData),
    getEnrollmentsByUser: (userId) => apiCall(`/enrollments/user/${userId}`)
};

// Export APIs for global use
window.userAPI = userAPI;
window.courseAPI = courseAPI;
window.enrollmentAPI = enrollmentAPI;
window.API_BASE_URL = API_BASE_URL;

console.log('🚀 API Module Loaded - Base URL:', API_BASE_URL);