// fetch.js
// cluster ip 10.102.35.181
const EXTERNAL_IP = 'http://localhost:8091'; // istio-ingressgateway의 EXTERNAL-IP

const BB_BASE_URL = `${EXTERNAL_IP}/bb-server`;
const SB_BASE_URL = `${EXTERNAL_IP}/sb-server`;
const CM_BASE_URL = `${EXTERNAL_IP}/cm-server`;
const Login_BASE_URL = `${EXTERNAL_IP}/login-server`



export { EXTERNAL_IP, BB_BASE_URL, SB_BASE_URL, CM_BASE_URL, Login_BASE_URL };