(global["webpackJsonp"] = global["webpackJsonp"] || []).push([["identity/filling/filling"],{

/***/ 126:
/*!************************************************************************************!*\
  !*** E:/hxds-cloud/hxds-driver-wx/main.js?{"page":"identity%2Ffilling%2Ffilling"} ***!
  \************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(wx, createPage) {

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ 4);
__webpack_require__(/*! uni-pages */ 26);
var _vue = _interopRequireDefault(__webpack_require__(/*! vue */ 25));
var _filling = _interopRequireDefault(__webpack_require__(/*! ./identity/filling/filling.vue */ 127));
// @ts-ignore
wx.__webpack_require_UNI_MP_PLUGIN__ = __webpack_require__;
createPage(_filling.default);
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/wx.js */ 1)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["createPage"]))

/***/ }),

/***/ 127:
/*!*****************************************************************!*\
  !*** E:/hxds-cloud/hxds-driver-wx/identity/filling/filling.vue ***!
  \*****************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./filling.vue?vue&type=template&id=c2ce9fa8& */ 128);
/* harmony import */ var _filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./filling.vue?vue&type=script&lang=js& */ 130);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_1__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_1__[key]; }) }(__WEBPACK_IMPORT_KEY__));
/* harmony import */ var _filling_vue_vue_type_style_index_0_lang_less___WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./filling.vue?vue&type=style&index=0&lang=less& */ 132);
/* harmony import */ var _D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/runtime/componentNormalizer.js */ 32);

var renderjs





/* normalize component */

var component = Object(_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_3__["default"])(
  _filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_1__["default"],
  _filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__["render"],
  _filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__["staticRenderFns"],
  false,
  null,
  null,
  null,
  false,
  _filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__["components"],
  renderjs
)

component.options.__file = "identity/filling/filling.vue"
/* harmony default export */ __webpack_exports__["default"] = (component.exports);

/***/ }),

/***/ 128:
/*!************************************************************************************************!*\
  !*** E:/hxds-cloud/hxds-driver-wx/identity/filling/filling.vue?vue&type=template&id=c2ce9fa8& ***!
  \************************************************************************************************/
/*! exports provided: render, staticRenderFns, recyclableRender, components */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--17-0!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/template.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-uni-app-loader/page-meta.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./filling.vue?vue&type=template&id=c2ce9fa8& */ 129);
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "render", function() { return _D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__["render"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "staticRenderFns", function() { return _D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__["staticRenderFns"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "recyclableRender", function() { return _D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__["recyclableRender"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "components", function() { return _D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_template_id_c2ce9fa8___WEBPACK_IMPORTED_MODULE_0__["components"]; });



/***/ }),

/***/ 129:
/*!************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--17-0!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/template.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-uni-app-loader/page-meta.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!E:/hxds-cloud/hxds-driver-wx/identity/filling/filling.vue?vue&type=template&id=c2ce9fa8& ***!
  \************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! exports provided: render, staticRenderFns, recyclableRender, components */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "render", function() { return render; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "staticRenderFns", function() { return staticRenderFns; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "recyclableRender", function() { return recyclableRender; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "components", function() { return components; });
var components
try {
  components = {
    uIcon: function () {
      return __webpack_require__.e(/*! import() | uview-ui/components/u-icon/u-icon */ "uview-ui/components/u-icon/u-icon").then(__webpack_require__.bind(null, /*! @/uview-ui/components/u-icon/u-icon.vue */ 290))
    },
    uCellGroup: function () {
      return __webpack_require__.e(/*! import() | uview-ui/components/u-cell-group/u-cell-group */ "uview-ui/components/u-cell-group/u-cell-group").then(__webpack_require__.bind(null, /*! @/uview-ui/components/u-cell-group/u-cell-group.vue */ 304))
    },
    uCellItem: function () {
      return __webpack_require__.e(/*! import() | uview-ui/components/u-cell-item/u-cell-item */ "uview-ui/components/u-cell-item/u-cell-item").then(__webpack_require__.bind(null, /*! @/uview-ui/components/u-cell-item/u-cell-item.vue */ 311))
    },
    uToast: function () {
      return __webpack_require__.e(/*! import() | uview-ui/components/u-toast/u-toast */ "uview-ui/components/u-toast/u-toast").then(__webpack_require__.bind(null, /*! @/uview-ui/components/u-toast/u-toast.vue */ 262))
    },
  }
} catch (e) {
  if (
    e.message.indexOf("Cannot find module") !== -1 &&
    e.message.indexOf(".vue") !== -1
  ) {
    console.error(e.message)
    console.error("1. 排查组件名称拼写是否正确")
    console.error(
      "2. 排查组件是否符合 easycom 规范，文档：https://uniapp.dcloud.net.cn/collocation/pages?id=easycom"
    )
    console.error(
      "3. 若组件不符合 easycom 规范，需手动引入，并在 components 中注册该组件"
    )
  } else {
    throw e
  }
}
var render = function () {
  var _vm = this
  var _h = _vm.$createElement
  var _c = _vm._self._c || _h
}
var recyclableRender = false
var staticRenderFns = []
render._withStripped = true



/***/ }),

/***/ 130:
/*!******************************************************************************************!*\
  !*** E:/hxds-cloud/hxds-driver-wx/identity/filling/filling.vue?vue&type=script&lang=js& ***!
  \******************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _D_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!./node_modules/babel-loader/lib!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./filling.vue?vue&type=script&lang=js& */ 131);
/* harmony import */ var _D_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_D_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _D_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _D_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_D_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 131:
/*!*************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/babel-loader/lib!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!E:/hxds-cloud/hxds-driver-wx/identity/filling/filling.vue?vue&type=script&lang=js& ***!
  \*************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(uni) {

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
function _createForOfIteratorHelper(o, allowArrayLike) { var it = typeof Symbol !== "undefined" && o[Symbol.iterator] || o["@@iterator"]; if (!it) { if (Array.isArray(o) || (it = _unsupportedIterableToArray(o)) || allowArrayLike && o && typeof o.length === "number") { if (it) o = it; var i = 0; var F = function F() {}; return { s: F, n: function n() { if (i >= o.length) return { done: true }; return { done: false, value: o[i++] }; }, e: function e(_e) { throw _e; }, f: F }; } throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); } var normalCompletion = true, didErr = false, err; return { s: function s() { it = it.call(o); }, n: function n() { var step = it.next(); normalCompletion = step.done; return step; }, e: function e(_e2) { didErr = true; err = _e2; }, f: function f() { try { if (!normalCompletion && it.return != null) it.return(); } finally { if (didErr) throw err; } } }; }
function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }
function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

var dayjs = __webpack_require__(/*! dayjs */ 85);
var _default = {
  data: function data() {
    return {
      mode: 'fill',
      style: {
        color: '#FF9900'
      },
      cardBackground: ['../static/filling/credentials-bg.jpg', '../static/filling/credentials-bg.jpg', '../static/filling/credentials-bg.jpg', '../static/filling/credentials-bg.jpg', '../static/filling/credentials-bg.jpg', '../static/filling/credentials-bg.jpg'],
      idcard: {
        pid: '',
        name: '',
        sex: '',
        address: '',
        shortAddress: '',
        birthday: '',
        expiration: '',
        idcardFront: '',
        idcardBack: '',
        idcardHolding: ''
      },
      contact: {
        tel: '',
        email: '',
        shortEmail: '',
        mailAddress: '',
        shortMailAddress: '',
        contactName: '',
        contactTel: ''
      },
      drcard: {
        issueDate: '',
        carClass: '',
        validFrom: '',
        validTo: '',
        drcardFront: '',
        drcardBack: '',
        drcardHolding: ''
      },
      cosImg: [],
      currentImg: {},
      realAuth: uni.getStorageSync('realAuth')
    };
  },
  methods: {
    scanIdcardFront: function scanIdcardFront(resp) {
      var that = this;
      var detail = resp.detail;
      that.idcard.pid = detail.id.text;
      that.idcard.name = detail.name.text;
      that.idcard.sex = detail.gender.text;
      that.idcard.address = detail.address.text;
      //需要缩略身份证地址，文字太长页面显示不了
      that.idcard.shortAddress = detail.address.text.substr(0, 15) + '...';
      that.idcard.birthday = detail.birth.text;
      //OCR插件拍摄到的身份证正面照片存储地址
      that.idcard.idcardFront = detail.image_path;
      //让身份证View标签加载身份证正面照片
      that.cardBackground[0] = detail.image_path;
      that.uploadCos(that.url.uploadCosPrivateFile, detail.image_path, 'driverAuth', function (resp) {
        var data = JSON.parse(resp.data);
        var path = data.path;
        that.currentImg['idcardFront'] = path;
        that.cosImg.push(path);
      });
    },
    scanIdcardBack: function scanIdcardBack(resp) {
      var that = this;
      var detail = resp.detail;
      //OCR插件拍摄到的身份证背面照片存储地址
      that.idcard.idcardBack = detail.image_path;
      //View标签加载身份证背面照片
      that.cardBackground[1] = detail.image_path;
      var validDate = detail.valid_date.text.split('-')[1];
      that.idcard.expiration = dayjs(validDate, 'YYYYMMDD').format('YYYY-MM-DD');
      that.uploadCos(that.url.uploadCosPrivateFile, detail.image_path, 'driverAuth', function (resp) {
        var data = JSON.parse(resp.data);
        var path = data.path;
        that.currentImg['idcardBack'] = path;
        that.cosImg.push(path);
      });
    },
    scanDrcardFront: function scanDrcardFront(resp) {
      var that = this;
      var detail = resp.detail;
      that.drcard.issueDate = detail.issue_date.text; //初次领证日期
      that.drcard.carClass = detail.car_class.text; //准驾车型
      that.drcard.validFrom = detail.valid_from.text; //驾驶证起始有效期
      that.drcard.validTo = detail.valid_to.text; //驾驶证截止有效期
      that.drcard.drcardFront = detail.image_path;
      that.cardBackground[3] = detail.image_path;
      that.uploadCos(that.url.uploadCosPrivateFile, detail.image_path, 'driverAuth', function (resp) {
        var data = JSON.parse(resp.data);
        var path = data.path;
        that.currentImg['drcardFront'] = path;
        that.cosImg.push(path);
      });
    },
    updatePhoto: function updatePhoto(type, path) {
      var that = this;
      that.uploadCos(that.url.uploadCosPrivateFile, path, 'driverAuth', function (resp) {
        var data = JSON.parse(resp.data);
        that.cosImg.push(data.path);
        if (type == 'idcardHolding') {
          that.cardBackground[2] = path;
          that.currentImg['idcardHolding'] = data.path;
          that.idcard.idcardHolding = data.path;
        } else if (type == 'drcardBack') {
          that.cardBackground[4] = path;
          that.currentImg['drcardBack'] = data.path;
          that.idcard.drcardBack = data.path;
        } else if (type == 'drcardHolding') {
          that.cardBackground[5] = path;
          that.currentImg['drcardHolding'] = data.path;
          that.idcard.drcardHolding = data.path;
        }
      });
      that.$forceUpdate();
    },
    takePhoto: function takePhoto(type) {
      uni.navigateTo({
        url: '../identity_camera/identity_camera?type=' + type
      });
    },
    enterContent: function enterContent(title, key) {
      var that = this;
      uni.showModal({
        title: title,
        editable: true,
        content: that.contact[key],
        success: function success(resp) {
          if (resp.confirm) {
            if (key == 'mailAddress') {
              that.contact['shortMailAddress'] = resp.content.substr(0, 15) + (resp.content.length > 15 ? '...' : '');
            } else if (key == 'email') {
              that.contact['shortEmail'] = resp.content.substr(0, 25) + (resp.content.length > 25 ? '...' : '');
            }
            that.contact[key] = resp.content;
          }
        }
      });
    },
    save: function save() {
      var that = this;
      //判断是否设置了6张照片
      if (Object.keys(that.currentImg).length != 6) {
        that.$refs.uToast.show({
          title: '证件上传不完整',
          type: 'error'
        });
      }
      //执行前端验证
      else if (that.checkValidTel(that.contact.tel, '手机号码') && that.checkValidEmail(that.contact.email, '电子信箱') && that.checkValidAddress(that.contact.mailAddress, '收信地址') && that.checkValidName(that.contact.contactName, '联系人') && that.checkValidTel(that.contact.contactTel, '联系人电话')) {
        uni.showModal({
          title: '提示信息',
          content: '确认提交实名资料？',
          success: function success(resp) {
            if (resp.confirm) {
              //比较哪些照片需要删除
              var temp = [];
              var values = [];
              //从JSON中获取6张证件照片的云端存储地址
              for (var key in that.currentImg) {
                var path = that.currentImg[key];
                values.push(path);
              }
              //判断cosImg数组里面哪些图片的云端地址不是6张图片的，这些图片要在云端删除
              var _iterator = _createForOfIteratorHelper(that.cosImg),
                _step;
              try {
                for (_iterator.s(); !(_step = _iterator.n()).done;) {
                  var one = _step.value;
                  if (!values.includes(one)) {
                    temp.push(one);
                  }
                }
              } catch (err) {
                _iterator.e(err);
              } finally {
                _iterator.f();
              }
              if (temp.length > 0) {
                //删除云端文件
                that.ajax(that.url.deleteCosPrivateFile, 'POST', JSON.stringify({
                  pathes: temp
                }), function () {
                  console.log('文件删除成功');
                });
              }
              //需要上传的实名认证数据
              var data = {
                pid: that.idcard.pid,
                name: that.idcard.name,
                sex: that.idcard.sex,
                birthday: that.idcard.birthday,
                tel: that.contact.tel,
                email: that.contact.email,
                mailAddress: that.contact.mailAddress,
                contactName: that.contact.contactName,
                contactTel: that.contact.contactTel,
                idcardAddress: that.idcard.address,
                idcardFront: that.currentImg.idcardFront,
                idcardBack: that.currentImg.idcardBack,
                idcardHolding: that.currentImg.idcardHolding,
                idcardExpiration: that.idcard.expiration,
                drcardType: that.drcard.carClass,
                drcardExpiration: that.drcard.validTo,
                drcardIssueDate: that.drcard.issueDate,
                drcardFront: that.currentImg.drcardFront,
                drcardBack: that.currentImg.drcardBack,
                drcardHolding: that.currentImg.drcardHolding
              };
              //提交Ajax请求，上传数据
              that.ajax(that.url.updateDriverAuth, 'POST', data, function (resp) {
                console.log('更新成功');
                that.$refs.uToast.show({
                  title: '资料提交成功',
                  type: 'success',
                  callback: function callback() {
                    uni.setStorageSync('realAuth', 3); //更新小程序Storage
                    that.realAuth = 3; //更新模型层
                    if (that.mode == 'create') {
                      //提示新注册的司机采集面部数据
                      uni.navigateTo({
                        url: "../face_camera/face_camera?mode=create"
                      });
                    } else {
                      //跳转到工作台页面
                      uni.switchTab({
                        url: '../../pages/workbench/workbench'
                      });
                    }
                  }
                });
              });
            }
          }
        });
      }
    },
    showAddressContent: function showAddressContent() {
      if (this.idcard.address.length > 0) {
        uni.showModal({
          title: '身份证地址',
          content: this.idcard.address,
          showCancel: false
        });
      }
    }
  },
  onLoad: function onLoad(options) {
    // console.log(uni.getStorageSync('token'));
    var that = this;
    that.mode = options.mode;
    if (uni.getStorageSync('realAuth') == 1) {
      uni.showModal({
        title: '提示信息',
        content: '新注册的代驾司机请填写实名认证信息，并且上传相关证件照片',
        showCancel: false
      });
    } else {
      that.ajax(that.url.searchDriverAuth, "GET", null, function (resp) {
        var json = resp.data.result;
        // console.log(json)
        that.idcard.pid = json.pid;
        that.idcard.name = json.name;
        that.idcard.sex = json.sex;
        that.idcard.birthday = json.birthday;
        that.idcard.address = json.idcardAddress;
        that.idcard.shortAddress = json.idcardAddress.substr(0, 15) + (json.idcardAddress.length > 15 ? '...' : '');
        that.idcard.expiration = json.idcardExpiration;
        that.idcard.idcardFront = json.idcardFront;
        if (json.idcardFrontUrl.length > 0) {
          that.cardBackground[0] = json.idcardFrontUrl;
        }
        that.idcard.idcardBack = json.idcardBack;
        if (json.idcardBackUrl.length > 0) {
          that.cardBackground[1] = json.idcardBackUrl;
        }
        that.idcard.idcardHolding = json.idcardHolding;
        if (json.idcardHoldingUrl.length > 0) {
          that.cardBackground[2] = json.idcardHoldingUrl;
        }
        that.contact.tel = json.tel;
        that.contact.email = json.email;
        that.contact.shortEmail = json.email.substr(0, 25) + (json.email.length > 25 ? '...' : '');
        that.contact.mailAddress = json.mailAddress;
        that.contact.shortMailAddress = json.mailAddress.substr(0, 15) + (json.mailAddress.length > 15 ? '...' : '');
        that.contact.contactName = json.contactName;
        that.contact.contactTel = json.contactTel;
        that.drcard.carClass = json.drcardType;
        that.drcard.validTo = json.drcardExpiration;
        that.drcard.issueDate = json.drcardIssueDate;
        that.drcard.drcardFront = json.drcardFront;
        if (json.drcardFrontUrl.length > 0) {
          that.cardBackground[3] = json.drcardFrontUrl;
        }
        that.drcard.drcardBack = json.drcardBack;
        if (json.drcardBackUrl.length > 0) {
          that.cardBackground[4] = json.drcardBackUrl;
        }
        that.drcard.drcardHolding = json.drcardHolding;
        if (json.drcardHoldingUrl.length > 0) {
          that.cardBackground[5] = json.drcardHoldingUrl;
        }
        if (that.idcard.idcardFront.length > 0) {
          that.cosImg.push(that.idcard.idcardFront);
          that.currentImg['idcardFront'] = that.idcard.idcardFront;
        }
        if (that.idcard.idcardBack.length > 0) {
          that.cosImg.push(that.idcard.idcardBack);
          that.currentImg['idcardBack'] = that.idcard.idcardBack;
        }
        if (that.idcard.idcardHolding.length > 0) {
          that.cosImg.push(that.idcard.idcardHolding);
          that.currentImg['idcardHolding'] = that.idcard.idcardHolding;
        }
        if (that.drcard.drcardFront.length > 0) {
          that.cosImg.push(that.drcard.drcardFront);
          that.currentImg['drcardFront'] = that.drcard.drcardFront;
        }
        if (that.drcard.drcardBack.length > 0) {
          that.cosImg.push(that.drcard.drcardBack);
          that.currentImg['drcardBack'] = that.drcard.drcardBack;
        }
        if (that.drcard.drcardHolding.length > 0) {
          that.cosImg.push(that.drcard.drcardHolding);
          that.currentImg['drcardHolding'] = that.drcard.drcardHolding;
        }
      });
    }
  }
};
exports.default = _default;
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["default"]))

/***/ }),

/***/ 132:
/*!***************************************************************************************************!*\
  !*** E:/hxds-cloud/hxds-driver-wx/identity/filling/filling.vue?vue&type=style&index=0&lang=less& ***!
  \***************************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _D_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_10_oneOf_1_0_D_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_10_oneOf_1_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_2_D_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_10_oneOf_1_3_D_HBuilderX_plugins_uniapp_cli_node_modules_less_loader_dist_cjs_js_ref_10_oneOf_1_4_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_5_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_style_index_0_lang_less___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!./node_modules/mini-css-extract-plugin/dist/loader.js??ref--10-oneOf-1-0!./node_modules/css-loader/dist/cjs.js??ref--10-oneOf-1-1!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--10-oneOf-1-2!./node_modules/postcss-loader/src??ref--10-oneOf-1-3!./node_modules/less-loader/dist/cjs.js??ref--10-oneOf-1-4!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--10-oneOf-1-5!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./filling.vue?vue&type=style&index=0&lang=less& */ 133);
/* harmony import */ var _D_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_10_oneOf_1_0_D_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_10_oneOf_1_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_2_D_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_10_oneOf_1_3_D_HBuilderX_plugins_uniapp_cli_node_modules_less_loader_dist_cjs_js_ref_10_oneOf_1_4_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_5_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_style_index_0_lang_less___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_D_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_10_oneOf_1_0_D_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_10_oneOf_1_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_2_D_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_10_oneOf_1_3_D_HBuilderX_plugins_uniapp_cli_node_modules_less_loader_dist_cjs_js_ref_10_oneOf_1_4_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_5_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_style_index_0_lang_less___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _D_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_10_oneOf_1_0_D_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_10_oneOf_1_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_2_D_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_10_oneOf_1_3_D_HBuilderX_plugins_uniapp_cli_node_modules_less_loader_dist_cjs_js_ref_10_oneOf_1_4_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_5_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_style_index_0_lang_less___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _D_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_10_oneOf_1_0_D_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_10_oneOf_1_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_2_D_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_10_oneOf_1_3_D_HBuilderX_plugins_uniapp_cli_node_modules_less_loader_dist_cjs_js_ref_10_oneOf_1_4_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_5_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_style_index_0_lang_less___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_D_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_10_oneOf_1_0_D_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_10_oneOf_1_1_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_2_D_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_10_oneOf_1_3_D_HBuilderX_plugins_uniapp_cli_node_modules_less_loader_dist_cjs_js_ref_10_oneOf_1_4_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_10_oneOf_1_5_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_D_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_filling_vue_vue_type_style_index_0_lang_less___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 133:
/*!***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/mini-css-extract-plugin/dist/loader.js??ref--10-oneOf-1-0!./node_modules/css-loader/dist/cjs.js??ref--10-oneOf-1-1!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--10-oneOf-1-2!./node_modules/postcss-loader/src??ref--10-oneOf-1-3!./node_modules/less-loader/dist/cjs.js??ref--10-oneOf-1-4!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--10-oneOf-1-5!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!E:/hxds-cloud/hxds-driver-wx/identity/filling/filling.vue?vue&type=style&index=0&lang=less& ***!
  \***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin
    if(false) { var cssReload; }
  

/***/ })

},[[126,"common/runtime","common/vendor"]]]);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/identity/filling/filling.js.map