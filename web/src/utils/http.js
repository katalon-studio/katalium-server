import _ from 'lodash';
import jquery from 'jquery';
import uuidv4 from 'uuid/v4';

const http = {

  get(url, data, headers, dataType) {
    return this.send('get', url, data, headers, dataType);
  },

  put(url, data) {
    return this.send('put', url, data);
  },

  post(url, data, headers, useUrlEncoded) {
    return this.send('post', url, data, headers, null, useUrlEncoded);
  },

  delete(url, data) {
    return this.send('delete', url, data);
  },

  send(method, url, data, headers, dataType, useUrlEncoded) {
    const uuid = uuidv4();
    headers = _.merge({}, headers, {
      'X-Request-ID': uuid,
    });
    dataType = dataType || 'json';
    const contentType = useUrlEncoded ?
      'application/x-www-form-urlencoded; charset=UTF-8' :
      'application/json; charset=UTF-8';
    const promise = new Promise((resolve, reject) => {
      const configs = {
        contentType,
        dataType,
        url,
        headers,
        method,
        cache: false,
        beforeSend: () => {
          jquery('#request-progress').css('display', 'block');
          return true;
        },
        success: (response) => {
          resolve(response);
        },
        error: (jqXHR, textStatus, errorThrown) => {
          console.error('Failed to make request', method, url, textStatus, errorThrown);
          const response = jqXHR.responseJSON;

          let message = '';
          if (response) {
            if (response.message) {
              message = response.message;
            } else if (response.error_description) {
              message = response.error_description;
            } else {
              message = response.error;
            }
          } else if (errorThrown) {
            message = errorThrown;
          } else {
            message = textStatus;
          }
          if (message) {
            console.error(message, url);
          }

          let rejectData;
          if (response) {
            rejectData = response;
          } else {
            rejectData = message;
          }
          reject(rejectData);

          return null;
        },
      };
      if (data) {
        if (method === 'get' || useUrlEncoded) {
          configs.data = data;
          configs.traditional = true;
        } else {
          configs.data = JSON.stringify(data);
          configs.processData = false;
        }
      }
      jquery.ajax(configs);
    });
    return promise;
  },
};

export default http;
