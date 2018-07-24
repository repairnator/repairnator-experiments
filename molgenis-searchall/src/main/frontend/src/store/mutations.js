// @flow
import type {Result, State} from './state'

export const SET_SUBMITTED = '__SET_SUBMITTED__'
export const SET_SEARCHTERM = '__SET_SEARCHTERM__'
export const SET_RESULTS = '__SET_RESULTS__'
export const SET_ERROR = '__SET_ERROR__'

export default {
  [SET_SUBMITTED] (state: State, submitted: boolean) {
    state.submitted = submitted
  },
  [SET_SEARCHTERM] (state: State, searchterm: string) {
    state.query = searchterm
  },
  [SET_RESULTS] (state: State, result: Result) {
    state.result = result
  },
  [SET_ERROR] (state: State, error: any) {
    state.error = error
  }
}
