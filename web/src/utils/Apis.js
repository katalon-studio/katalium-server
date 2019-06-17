import jquery from 'jquery';

const getQueryString = (urlObject) => {
  if (urlObject) {
    return `?${jquery.param(urlObject, true)}`;
  }
  return '';
};

const Apis = {
  getQueryString,

  sessions: (queryInfo) => {
    const path = '/grid/admin/ListSessionsServlet';
    const queryString = getQueryString(queryInfo);
    return path + queryString;
  },

  session: (sessionId, queryInfo) => {
    const path = '/grid/admin/SessionServlet/' + sessionId;
    const queryString = getQueryString(queryInfo);
    return path + queryString;
  },

  screenshots: (sessionId) => {
    const path = '/grid/admin/ScreenShotsServlet';
    const queryString = getQueryString({ sessionId: sessionId });
    return path + queryString;
  },
};

export default Apis;
