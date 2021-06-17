let isJSON = function (str) {
    if (str == '' || str == null) {
        return true
    }
    if (typeof (str) === 'string') {
        try {
            var obj = JSON.parse(str);
            if (typeof (obj) === 'object' && obj && obj && !(obj instanceof Array)) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (e) {
            return false;
        }
    }
}
export default isJSON;