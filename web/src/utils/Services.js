import Apis from './Apis';
import http from './http';

const Services = {

  getSessions() {
    return http.get(Apis.sessions());
  },

  getSession(sessionId) {
    return http.get(Apis.session(sessionId));
  },

  getScreenshots(sessionId) {
    return http.get(Apis.screenshots(sessionId));
  },
};

export default Services;
